package com.example.trackwheel.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.trackwheel.db.converters.DateConverter
import java.util.Date

@Entity(tableName = "trips")
@TypeConverters(DateConverter::class)
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Date,
    val distance: Double,  // in kilometers
    val duration: Double,  // in minutes
    val averageSpeed: Double  // in km/h
)
