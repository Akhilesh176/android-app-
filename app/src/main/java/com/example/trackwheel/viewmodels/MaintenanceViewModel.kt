package com.example.trackwheel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackwheel.db.entities.MaintenanceTask
import com.example.trackwheel.repositories.MaintenanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaintenanceViewModel @Inject constructor(
    private val repository: MaintenanceRepository
) : ViewModel() {
    
    private val _maintenanceTasks = MutableLiveData<List<MaintenanceTask>>()
    val maintenanceTasks: LiveData<List<MaintenanceTask>> = _maintenanceTasks
    
    init {
        loadUpcomingTasks()
    }
    
    fun loadUpcomingTasks() {
        viewModelScope.launch {
            _maintenanceTasks.value = repository.upcomingTasks.value ?: emptyList()
        }
    }
    
    fun loadCompletedTasks() {
        viewModelScope.launch {
            _maintenanceTasks.value = repository.completedTasks.value ?: emptyList()
        }
    }
    
    fun insertMaintenanceTask(task: MaintenanceTask) {
        viewModelScope.launch {
            repository.insertTask(task)
            loadUpcomingTasks()
        }
    }
    
    fun updateMaintenanceTask(task: MaintenanceTask) {
        viewModelScope.launch {
            repository.updateTask(task)
            if (task.isCompleted) {
                loadCompletedTasks()
            } else {
                loadUpcomingTasks()
            }
        }
    }
    
    fun deleteMaintenanceTask(task: MaintenanceTask) {
        viewModelScope.launch {
            repository.deleteTask(task)
            if (task.isCompleted) {
                loadCompletedTasks()
            } else {
                loadUpcomingTasks()
            }
        }
    }
}
