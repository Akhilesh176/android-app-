package com.example.trackwheel.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackwheel.db.entities.FuelEntry

@Dao
interface FuelEntryDao {
    @Query("SELECT * FROM fuel_entries ORDER BY date DESC")
    fun getAllFuelEntries(): LiveData<List<FuelEntry>>
    
    @Query("SELECT * FROM fuel_entries ORDER BY date DESC LIMIT :limit")
    fun getRecentFuelEntries(limit: Int): LiveData<List<FuelEntry>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFuelEntry(fuelEntry: FuelEntry)
    
    @Delete
    suspend fun deleteFuelEntry(fuelEntry: FuelEntry)
}
