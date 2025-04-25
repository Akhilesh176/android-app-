package com.example.trackwheel.repositories

import androidx.lifecycle.LiveData
import com.example.trackwheel.db.dao.TripDao
import com.example.trackwheel.db.entities.Trip
import javax.inject.Inject

class TripRepository @Inject constructor(
    private val tripDao: TripDao
) {
    val allTrips: LiveData<List<Trip>> = tripDao.getAllTrips()
    
    fun getRecentTrips(limit: Int): LiveData<List<Trip>> {
        return tripDao.getRecentTrips(limit)
    }
    
    suspend fun insertTrip(trip: Trip) {
        tripDao.insertTrip(trip)
    }
    
    suspend fun deleteTrip(trip: Trip) {
        tripDao.deleteTrip(trip)
    }
}
