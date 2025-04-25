package com.example.trackwheel.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.trackwheel.db.converters.DateConverter
import java.util.Date

@Entity(tableName = "maintenance_tasks")
@TypeConverters(DateConverter::class)
data class MaintenanceTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val dueDate: Date,
    val notes: String?,
    val isCompleted: Boolean
)
