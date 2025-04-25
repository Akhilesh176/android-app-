package com.example.trackwheel.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackwheel.adapters.FuelEntryDetailedAdapter
import com.example.trackwheel.databinding.ActivityFuelLogBinding
import com.example.trackwheel.db.entities.FuelEntry
import com.example.trackwheel.viewmodels.FuelViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class FuelLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFuelLogBinding
    private val viewModel: FuelViewModel by viewModels()
    private lateinit var adapter: FuelEntryDetailedAdapter
    private var selectedDate: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Fuel Log"
        
        setupRecyclerView()
        setupDatePicker()
        setupSubmitButton()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.allFuelEntries.observe(this, Observer { entries ->
            adapter.submitList(entries)
        })
    }

    private fun setupRecyclerView() {
        adapter = FuelEntryDetailedAdapter { fuelEntry ->
            viewModel.deleteFuelEntry(fuelEntry)
            Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show()
        }
        binding.fuelEntriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@FuelLogActivity)
            adapter = this@FuelLogActivity.adapter
        }
    }

    private fun setupDatePicker() {
        // Set initial date
        updateDateButtonText()
        
        binding.dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate
            
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedDate = calendar.time
                    updateDateButtonText()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
    
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Refresh data
            viewModel.loadAllFuelEntries()
            
            // Hide the refresh indicator after a delay
            binding.swipeRefreshLayout.postDelayed({
                binding.swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this, "Data refreshed", Toast.LENGTH_SHORT).show()
            }, 1000)
        }
        
        // Set the colors for the refresh animation
        binding.swipeRefreshLayout.setColorSchemeResources(
            com.example.trackwheel.R.color.primary,
            com.example.trackwheel.R.color.accent
        )
    }

    private fun updateDateButtonText() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        binding.dateButton.text = dateFormat.format(selectedDate)
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            if (validateInputs()) {
                val amount = binding.fuelAmountEditText.text.toString().toFloat()
                val price = binding.fuelPriceEditText.text.toString().toFloat()
                val odometer = binding.odometerEditText.text.toString().toInt()
                
                val fuelEntry = FuelEntry(
                    id = 0, // Room will auto-generate
                    date = selectedDate,
                    amount = amount,
                    price = price,
                    odometer = odometer
                )
                
                viewModel.insertFuelEntry(fuelEntry)
                
                // Clear inputs
                binding.fuelAmountEditText.text?.clear()
                binding.fuelPriceEditText.text?.clear()
                binding.odometerEditText.text?.clear()
                
                Toast.makeText(this, "Fuel entry added successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        
        if (binding.fuelAmountEditText.text.isNullOrEmpty()) {
            binding.fuelAmountEditText.error = "Required"
            isValid = false
        }
        
        if (binding.fuelPriceEditText.text.isNullOrEmpty()) {
            binding.fuelPriceEditText.error = "Required"
            isValid = false
        }
        
        if (binding.odometerEditText.text.isNullOrEmpty()) {
            binding.odometerEditText.error = "Required"
            isValid = false
        }
        
        return isValid
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
