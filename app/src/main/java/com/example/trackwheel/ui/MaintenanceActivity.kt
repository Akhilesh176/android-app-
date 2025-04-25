package com.example.trackwheel.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackwheel.adapters.MaintenanceDetailedAdapter
import com.example.trackwheel.databinding.ActivityMaintenanceBinding
import com.example.trackwheel.db.entities.MaintenanceTask
import com.example.trackwheel.viewmodels.MaintenanceViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MaintenanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaintenanceBinding
    private val viewModel: MaintenanceViewModel by viewModels()
    private lateinit var adapter: MaintenanceDetailedAdapter
    private var selectedDate: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaintenanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Maintenance Tracker"
        
        setupRecyclerView()
        setupDatePicker()
        setupSubmitButton()
        setupTabButtons()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.maintenanceTasks.observe(this, Observer { tasks ->
            adapter.submitList(tasks)
        })
    }

    private fun setupRecyclerView() {
        adapter = MaintenanceDetailedAdapter(
            onDeleteClick = { task ->
                viewModel.deleteMaintenanceTask(task)
                Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
            },
            onCompletedChange = { task, isCompleted ->
                viewModel.updateMaintenanceTask(task.copy(isCompleted = isCompleted))
            }
        )
        binding.maintenanceRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MaintenanceActivity)
            adapter = this@MaintenanceActivity.adapter
        }
    }

    private fun setupDatePicker() {
        // Set initial date
        updateDateButtonText()
        
        binding.dueDateButton.setOnClickListener {
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

    private fun updateDateButtonText() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        binding.dueDateButton.text = dateFormat.format(selectedDate)
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            if (validateInputs()) {
                val description = binding.descriptionEditText.text.toString()
                val notes = binding.notesEditText.text.toString()
                
                val task = MaintenanceTask(
                    id = 0, // Room will auto-generate
                    description = description,
                    dueDate = selectedDate,
                    notes = notes,
                    isCompleted = false
                )
                
                viewModel.insertMaintenanceTask(task)
                
                // Clear inputs
                binding.descriptionEditText.text?.clear()
                binding.notesEditText.text?.clear()
                
                Toast.makeText(this, "Maintenance task added successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupTabButtons() {
        binding.upcomingButton.setOnClickListener {
            binding.upcomingButton.isSelected = true
            binding.completedButton.isSelected = false
            viewModel.loadUpcomingTasks()
        }
        
        binding.completedButton.setOnClickListener {
            binding.upcomingButton.isSelected = false
            binding.completedButton.isSelected = true
            viewModel.loadCompletedTasks()
        }
        
        // Set default selection
        binding.upcomingButton.isSelected = true
        viewModel.loadUpcomingTasks()
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        
        if (binding.descriptionEditText.text.isNullOrEmpty()) {
            binding.descriptionEditText.error = "Required"
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
