package com.example.trackwheel.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.trackwheel.db.converters.DateConverter
import java.util.Date

@Entity(tableName = "fuel_entries")
@TypeConverters(DateConverter::class)
data class FuelEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Date,
    val amount: Float,  // in liters
    val price: Float,   // total price
    val odometer: Int   // in kilometers
)
