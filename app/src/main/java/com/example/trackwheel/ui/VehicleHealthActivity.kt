package com.example.trackwheel.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.trackwheel.databinding.ActivityVehicleHealthBinding
import com.example.trackwheel.viewmodels.VehicleHealthViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.Locale

class VehicleHealthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleHealthBinding
    private lateinit var viewModel: VehicleHealthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleHealthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Vehicle Health Report"
        
        setupViewModel()
        setupCharts()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[VehicleHealthViewModel::class.java]
        
        viewModel.fuelEfficiencyData.observe(this) { data ->
            updateFuelEfficiencyChart(data)
            
            // Update summary stats
            if (data.isNotEmpty()) {
                val avgEfficiency = data.map { it.second }.average()
                binding.avgFuelEfficiencyValue.text = String.format("%.2f km/L", avgEfficiency)
            }
        }
        
        viewModel.maintenanceCompletionData.observe(this) { data ->
            // Update maintenance stats
            val totalTasks = data.first
            val completedTasks = data.second
            val completionRate = if (totalTasks > 0) (completedTasks.toFloat() / totalTasks) * 100 else 0f
            
            binding.maintenanceCompletionValue.text = String.format("%.1f%%", completionRate)
            binding.tasksCompletedValue.text = "$completedTasks of $totalTasks"
        }
    }

    private fun setupCharts() {
        // Fuel Efficiency Chart
        binding.fuelEfficiencyChart.apply {
            description.isEnabled = false
            legend.isEnabled = true
            setDrawGridBackground(false)
            setTouchEnabled(true)
            setScaleEnabled(true)
            
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            
            axisLeft.setDrawGridLines(true)
            axisRight.isEnabled = false
        }
        
        // Load data
        viewModel.loadFuelEfficiencyData()
        viewModel.loadMaintenanceCompletionData()
    }

    private fun updateFuelEfficiencyChart(data: List<Pair<Long, Double>>) {
        if (data.isEmpty()) return
        
        val entries = ArrayList<Entry>()
        val labels = ArrayList<String>()
        val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
        
        data.forEachIndexed { index, (timestamp, efficiency) ->
            entries.add(Entry(index.toFloat(), efficiency.toFloat()))
            labels.add(dateFormat.format(timestamp))
        }
        
        val dataSet = LineDataSet(entries, "Fuel Efficiency (km/L)")
        dataSet.color = ColorTemplate.MATERIAL_COLORS[0]
        dataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[0])
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawCircleHole(false)
        dataSet.valueTextSize = 10f
        dataSet.setDrawFilled(true)
        dataSet.fillColor = ColorTemplate.MATERIAL_COLORS[0]
        dataSet.fillAlpha = 50
        
        val lineData = LineData(dataSet)
        
        binding.fuelEfficiencyChart.data = lineData
        binding.fuelEfficiencyChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.fuelEfficiencyChart.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
