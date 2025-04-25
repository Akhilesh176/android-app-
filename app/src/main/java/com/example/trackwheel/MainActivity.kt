package com.example.trackwheel

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackwheel.adapters.FuelEntryAdapter
import com.example.trackwheel.adapters.MaintenanceAdapter
import com.example.trackwheel.data.preferences.UserPreferences
import com.example.trackwheel.databinding.ActivityMainBinding
import com.example.trackwheel.ui.FuelLogActivity
import com.example.trackwheel.ui.LoginActivity
import com.example.trackwheel.ui.MaintenanceActivity
import com.example.trackwheel.ui.TripAnalyticsActivity
import com.example.trackwheel.ui.VehicleHealthActivity
import com.example.trackwheel.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var fuelAdapter: FuelEntryAdapter
    private lateinit var maintenanceAdapter: MaintenanceAdapter
    
    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecyclerViews()
        setupClickListeners()
        setupSwipeRefresh()
        setupProfileSection()
        observeViewModel()
        updateDashboardInfo()
    }

    private fun observeViewModel() {
        viewModel.recentFuelEntries.observe(this, Observer { entries ->
            fuelAdapter.submitList(entries)
            if (entries.isNotEmpty()) {
                val latestEntry = entries[0]
                binding.lastFuelAmountValue.text = String.format("%.2f L", latestEntry.amount)
                binding.lastFuelPriceValue.text = String.format("$%.2f", latestEntry.price)
                binding.lastFuelDateValue.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(latestEntry.date)
            }
        })
        
        viewModel.upcomingMaintenance.observe(this, Observer { tasks ->
            maintenanceAdapter.submitList(tasks)
            if (tasks.isNotEmpty()) {
                val nextTask = tasks[0]
                binding.nextMaintenanceValue.text = nextTask.description
                binding.nextMaintenanceDateValue.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(nextTask.dueDate)
            }
        })
        
        viewModel.recentTrips.observe(this, Observer { trips ->
            if (trips.isNotEmpty()) {
                val latestTrip = trips[0]
                binding.lastTripDistanceValue.text = String.format("%.1f km", latestTrip.distance)
                binding.lastTripDurationValue.text = String.format("%.1f min", latestTrip.duration)
                binding.lastTripDateValue.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(latestTrip.date)
            }
        })
    }

    private fun setupRecyclerViews() {
        fuelAdapter = FuelEntryAdapter()
        binding.recentFuelRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = fuelAdapter
        }
        
        maintenanceAdapter = MaintenanceAdapter()
        binding.upcomingMaintenanceRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = maintenanceAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fuelCardView.setOnClickListener {
            startActivity(Intent(this, FuelLogActivity::class.java))
        }
        
        binding.maintenanceCardView.setOnClickListener {
            startActivity(Intent(this, MaintenanceActivity::class.java))
        }
        
        binding.tripsCardView.setOnClickListener {
            startActivity(Intent(this, TripAnalyticsActivity::class.java))
        }
        
        binding.healthCardView.setOnClickListener {
            startActivity(Intent(this, VehicleHealthActivity::class.java))
        }
        
        binding.fabAdd.setOnClickListener {
            showAddOptionsMenu()
        }
        
        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }
        
        binding.profileImage.setOnClickListener {
            // Show profile options
            Toast.makeText(this, "Profile options coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Refresh data
            viewModel.refreshData()
            
            // Hide the refresh indicator after a delay
            binding.swipeRefreshLayout.postDelayed({
                binding.swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this, "Data refreshed", Toast.LENGTH_SHORT).show()
            }, 1000)
        }
        
        // Set the colors for the refresh animation
        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.primary,
            R.color.accent
        )
    }
    
    private fun setupProfileSection() {
        lifecycleScope.launch {
            val email = userPreferences.userEmail.first()
            binding.userEmailText.text = email
            
            // Extract name from email (for demo purposes)
            val name = email.split("@").firstOrNull() ?: "User"
            binding.userNameText.text = name.capitalize()
        }
    }

    private fun updateDashboardInfo() {
        // Update current date
        val currentDate = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(Date())
        binding.currentDateText.text = currentDate
    }
    
    private fun showAddOptionsMenu() {
        val popupMenu = PopupMenu(this, binding.fabAdd)
        popupMenu.menuInflater.inflate(R.menu.menu_add, popupMenu.menu)
        
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_add_fuel -> {
                    startActivity(Intent(this, FuelLogActivity::class.java))
                    true
                }
                R.id.action_add_maintenance -> {
                    startActivity(Intent(this, MaintenanceActivity::class.java))
                    true
                }
                R.id.action_add_trip -> {
                    startActivity(Intent(this, TripAnalyticsActivity::class.java))
                    true
                }
                else -> false
            }
        }
        
        popupMenu.show()
    }
    
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                performLogout()
            }
            .setNegativeButton("No", null)
            .show()
    }
    
    private fun performLogout() {
        lifecycleScope.launch {
            userPreferences.clearUserData()
            
            // Navigate to login screen
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Handle settings action
                Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
