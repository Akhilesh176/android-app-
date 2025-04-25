package com.example.trackwheel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackwheel.repositories.FuelRepository
import com.example.trackwheel.repositories.MaintenanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleHealthViewModel @Inject constructor(
    private val fuelRepository: FuelRepository,
    private val maintenanceRepository: MaintenanceRepository
) : ViewModel() {
    
    private val _fuelEfficiencyData = MutableLiveData<List<Pair<Long, Double>>>()
    val fuelEfficiencyData: LiveData<List<Pair<Long, Double>>> = _fuelEfficiencyData
    
    private val _maintenanceCompletionData = MutableLiveData<Pair<Int, Int>>()
    val maintenanceCompletionData: LiveData<Pair<Int, Int>> = _maintenanceCompletionData
    
    fun loadFuelEfficiencyData() {
        viewModelScope.launch {
            // In a real app, this would calculate efficiency from consecutive fuel entries
            // For demo purposes, we'll use sample data
            val sampleData = listOf(
                Pair(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000L, 10.5),
                Pair(System.currentTimeMillis() - 25 * 24 * 60 * 60 * 1000L, 11.2),
                Pair(System.currentTimeMillis() - 20 * 24 * 60 * 60 * 1000L, 10.8),
                Pair(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000L, 11.5),
                Pair(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000L, 12.0),
                Pair(System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000L, 11.7),
                Pair(System.currentTimeMillis(), 12.3)
            )
            _fuelEfficiencyData.value = sampleData
        }
    }
    
    fun loadMaintenanceCompletionData() {
        viewModelScope.launch {
            val totalTasks = maintenanceRepository.getTotalTaskCount()
            val completedTasks = maintenanceRepository.getCompletedTaskCount()
            _maintenanceCompletionData.value = Pair(totalTasks, completedTasks)
        }
    }
}
