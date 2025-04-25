package com.example.trackwheel.repositories

import androidx.lifecycle.LiveData
import com.example.trackwheel.db.dao.MaintenanceTaskDao
import com.example.trackwheel.db.entities.MaintenanceTask
import javax.inject.Inject

class MaintenanceRepository @Inject constructor(
    private val maintenanceTaskDao: MaintenanceTaskDao
) {
    val upcomingTasks: LiveData<List<MaintenanceTask>> = maintenanceTaskDao.getUpcomingTasks()
    val completedTasks: LiveData<List<MaintenanceTask>> = maintenanceTaskDao.getCompletedTasks()
    
    fun getUpcomingTasksLimited(limit: Int): LiveData<List<MaintenanceTask>> {
        return maintenanceTaskDao.getUpcomingTasksLimited(limit)
    }
    
    suspend fun getTotalTaskCount(): Int {
        return maintenanceTaskDao.getTotalTaskCount()
    }
    
    suspend fun getCompletedTaskCount(): Int {
        return maintenanceTaskDao.getCompletedTaskCount()
    }
    
    suspend fun insertTask(task: MaintenanceTask) {
        maintenanceTaskDao.insertTask(task)
    }
    
    suspend fun updateTask(task: MaintenanceTask) {
        maintenanceTaskDao.updateTask(task)
    }
    
    suspend fun deleteTask(task: MaintenanceTask) {
        maintenanceTaskDao.deleteTask(task)
    }
}
