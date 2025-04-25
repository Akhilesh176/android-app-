package com.example.trackwheel.repositories

import androidx.lifecycle.LiveData
import com.example.trackwheel.db.dao.FuelEntryDao
import com.example.trackwheel.db.entities.FuelEntry
import javax.inject.Inject

class FuelRepository @Inject constructor(
    private val fuelEntryDao: FuelEntryDao
) {
    val allFuelEntries: LiveData<List<FuelEntry>> = fuelEntryDao.getAllFuelEntries()
    
    fun getRecentFuelEntries(limit: Int): LiveData<List<FuelEntry>> {
        return fuelEntryDao.getRecentFuelEntries(limit)
    }
    
    suspend fun insertFuelEntry(fuelEntry: FuelEntry) {
        fuelEntryDao.insertFuelEntry(fuelEntry)
    }
    
    suspend fun deleteFuelEntry(fuelEntry: FuelEntry) {
        fuelEntryDao.deleteFuelEntry(fuelEntry)
    }
}
