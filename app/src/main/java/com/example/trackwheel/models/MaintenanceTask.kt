package com.example.trackwheel.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "maintenance_tasks")
data class MaintenanceTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String,
    val dueDate: Date,
    val notes: String?,
    val isCompleted: Boolean
)
