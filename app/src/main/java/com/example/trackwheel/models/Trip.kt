package com.example.trackwheel.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: Date,
    val distance: Double,  // in kilometers
    val duration: Double,  // in minutes
    val averageSpeed: Double  // in km/h
)
