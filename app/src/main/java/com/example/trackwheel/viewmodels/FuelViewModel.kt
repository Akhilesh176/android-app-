package com.example.trackwheel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackwheel.db.entities.FuelEntry
import com.example.trackwheel.repositories.FuelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FuelViewModel @Inject constructor(
    private val repository: FuelRepository
) : ViewModel() {
    
    val allFuelEntries: LiveData<List<FuelEntry>> = repository.allFuelEntries
    
    fun insertFuelEntry(fuelEntry: FuelEntry) {
        viewModelScope.launch {
            repository.insertFuelEntry(fuelEntry)
        }
    }
    
    fun deleteFuelEntry(fuelEntry: FuelEntry) {
        viewModelScope.launch {
            repository.deleteFuelEntry(fuelEntry)
        }
    }
}
