package com.example.trackwheel.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "fuel_entries")
data class FuelEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: Date,
    val amount: Float,  // in liters
    val price: Float,   // total price
    val odometer: Int   // in kilometers
)
