package com.example.trackwheel.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.trackwheel.databinding.ActivityTripAnalyticsBinding
import com.example.trackwheel.viewmodels.TripViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.Locale

class TripAnalyticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripAnalyticsBinding
    private lateinit var viewModel: TripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Trip Analytics"
        
        setupViewModel()
        setupCharts()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[TripViewModel::class.java]
        
        viewModel.trips.observe(this) { trips ->
            updateDistanceChart(trips)
            updateSpeedChart(trips)
            
            // Update summary stats
            if (trips.isNotEmpty()) {
                val totalDistance = trips.sumOf { it.distance }
                val avgSpeed = trips.map { it.averageSpeed }.average()
                val totalDuration = trips.sumOf { it.duration }
                
                binding.totalDistanceValue.text = String.format("%.1f km", totalDistance)
                binding.avgSpeedValue.text = String.format("%.1f km/h", avgSpeed)
                binding.totalDurationValue.text = String.format("%.1f min", totalDuration)
            }
        }
    }

    private fun setupCharts() {
        // Distance Chart
        binding.distanceChart.apply {
            description.isEnabled = false
            legend.isEnabled = true
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setScaleEnabled(false)
            
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            
            axisLeft.setDrawGridLines(true)
            axisRight.isEnabled = false
        }
        
        // Speed Chart
        binding.speedChart.apply {
            description.isEnabled = false
            legend.isEnabled = true
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setScaleEnabled(false)
            
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            
            axisLeft.setDrawGridLines(true)
            axisRight.isEnabled = false
        }
        
        // Load data
        viewModel.loadTrips()
    }

    private fun updateDistanceChart(trips: List<com.example.trackwheel.models.Trip>) {
        if (trips.isEmpty()) return
        
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
        
        // Get last 7 trips or fewer
        val recentTrips = trips.takeLast(7)
        
        recentTrips.forEachIndexed { index, trip ->
            entries.add(BarEntry(index.toFloat(), trip.distance.toFloat()))
            labels.add(dateFormat.format(trip.date))
        }
        
        val dataSet = BarDataSet(entries, "Distance (km)")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        
        val barData = BarData(dataSet)
        barData.barWidth = 0.6f
        
        binding.distanceChart.data = barData
        binding.distanceChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.distanceChart.invalidate()
    }

    private fun updateSpeedChart(trips: List<com.example.trackwheel.models.Trip>) {
        if (trips.isEmpty()) return
        
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
        
        // Get last 7 trips or fewer
        val recentTrips = trips.takeLast(7)
        
        recentTrips.forEachIndexed { index, trip ->
            entries.add(BarEntry(index.toFloat(), trip.averageSpeed.toFloat()))
            labels.add(dateFormat.format(trip.date))
        }
        
        val dataSet = BarDataSet(entries, "Avg Speed (km/h)")
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()
        
        val barData = BarData(dataSet)
        barData.barWidth = 0.6f
        
        binding.speedChart.data = barData
        binding.speedChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.speedChart.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
