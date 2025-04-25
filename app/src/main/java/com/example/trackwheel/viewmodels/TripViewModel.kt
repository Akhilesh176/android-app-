package com.example.trackwheel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackwheel.db.entities.Trip
import com.example.trackwheel.repositories.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {
    
    val trips: LiveData<List<Trip>> = repository.allTrips
    
    fun insertTrip(trip: Trip) {
        viewModelScope.launch {
            repository.insertTrip(trip)
        }
    }
    
    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            repository.deleteTrip(trip)
        }
    }
}
