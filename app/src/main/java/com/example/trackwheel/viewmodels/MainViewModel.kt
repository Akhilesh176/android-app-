package com.example.trackwheel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackwheel.db.entities.FuelEntry
import com.example.trackwheel.db.entities.MaintenanceTask
import com.example.trackwheel.db.entities.Trip
import com.example.trackwheel.repositories.FuelRepository
import com.example.trackwheel.repositories.MaintenanceRepository
import com.example.trackwheel.repositories.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fuelRepository: FuelRepository,
    private val maintenanceRepository: MaintenanceRepository,
    private val tripRepository: TripRepository
) : ViewModel() {
    
    val recentFuelEntries: LiveData<List<FuelEntry>> = fuelRepository.getRecentFuelEntries(3)
    val upcomingMaintenance: LiveData<List<MaintenanceTask>> = maintenanceRepository.getUpcomingTasksLimited(3)
    val recentTrips: LiveData<List<Trip>> = tripRepository.getRecentTrips(3)
    
    // This method is called when the user swipes to refresh
    fun refreshData() {
        viewModelScope.launch {
            // In a real app, you would refresh data from a remote source
            // For this demo, we'll just trigger a reload from the database
            // The LiveData will automatically update the UI
        }
    }
}
