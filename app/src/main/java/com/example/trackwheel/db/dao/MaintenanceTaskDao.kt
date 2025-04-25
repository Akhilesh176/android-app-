package com.example.trackwheel.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackwheel.db.entities.MaintenanceTask

@Dao
interface MaintenanceTaskDao {
    @Query("SELECT * FROM maintenance_tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getUpcomingTasks(): LiveData<List<MaintenanceTask>>
    
    @Query("SELECT * FROM maintenance_tasks WHERE isCompleted = 1 ORDER BY dueDate DESC")
    fun getCompletedTasks(): LiveData<List<MaintenanceTask>>
    
    @Query("SELECT * FROM maintenance_tasks WHERE isCompleted = 0 ORDER BY dueDate ASC LIMIT :limit")
    fun getUpcomingTasksLimited(limit: Int): LiveData<List<MaintenanceTask>>
    
    @Query("SELECT COUNT(*) FROM maintenance_tasks")
    suspend fun getTotalTaskCount(): Int
    
    @Query("SELECT COUNT(*) FROM maintenance_tasks WHERE isCompleted = 1")
    suspend fun getCompletedTaskCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: MaintenanceTask)
    
    @Update
    suspend fun updateTask(task: MaintenanceTask)
    
    @Delete
    suspend fun deleteTask(task: MaintenanceTask)
}
