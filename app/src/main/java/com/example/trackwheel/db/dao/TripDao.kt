package com.example.trackwheel.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackwheel.db.entities.Trip

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY date DESC")
    fun getAllTrips(): LiveData<List<Trip>>
    
    @Query("SELECT * FROM trips ORDER BY date DESC LIMIT :limit")
    fun getRecentTrips(limit: Int): LiveData<List<Trip>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip)
    
    @Delete
    suspend fun deleteTrip(trip: Trip)
}
