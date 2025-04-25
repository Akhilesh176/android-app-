package com.example.trackwheel.data

import com.example.trackwheel.models.FuelEntry
import com.example.trackwheel.models.MaintenanceTask
import com.example.trackwheel.models.Trip
import java.util.Calendar
import java.util.Date

class AppRepository private constructor() {
    
    // For demo purposes, we'll use in-memory data
    // In a real app, this would use Room database
    
    private val fuelEntries = mutableListOf<FuelEntry>()
    private val maintenanceTasks = mutableListOf<MaintenanceTask>()
    private val trips = mutableListOf<Trip>()
    
    init {
        // Add some sample data
        populateSampleData()
    }
    
    private fun populateSampleData() {
        // Sample fuel entries
        val calendar = Calendar.getInstance()
        
        // Current date
        val today = calendar.time
        
        // 7 days ago
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        val sevenDaysAgo = calendar.time
        
        // 14 days ago
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        val fourteenDaysAgo = calendar.time
        
        // 21 days ago
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        val twentyOneDaysAgo = calendar.time
        
        fuelEntries.add(FuelEntry(1, today, 45.5f, 65.75f, 12345))
        fuelEntries.add(FuelEntry(2, sevenDaysAgo, 42.0f, 60.90f, 12100))
        fuelEntries.add(FuelEntry(3, fourteenDaysAgo, 48.2f, 69.89f, 11850))
        fuelEntries.add(FuelEntry(4, twentyOneDaysAgo, 40.8f, 59.16f, 11600))
        
        // Sample maintenance tasks
        calendar.time = today
        calendar.add(Calendar.DAY_OF_MONTH, 30)
        val thirtyDaysLater = calendar.time
        
        calendar.time = today
        calendar.add(Calendar.DAY_OF_MONTH, 15)
        val fifteenDaysLater = calendar.time
        
        calendar.time = today
        calendar.add(Calendar.DAY_OF_MONTH, -15)
        val fifteenDaysAgo = calendar.time
        
        maintenanceTasks.add(MaintenanceTask(1, "Oil Change", thirtyDaysLater, "Use synthetic oil 5W-30", false))
        maintenanceTasks.ad  "Oil Change", thirtyDaysLater, "Use synthetic oil 5W-30", false))
        maintenanceTasks.add(MaintenanceTask(2, "Tire Rotation", fifteenDaysLater, "Check tire pressure as well", false))
        maintenanceTasks.add(MaintenanceTask(3, "Air Filter Replacement", thirtyDaysLater, null, false))
        maintenanceTasks.add(MaintenanceTask(4, "Brake Inspection", fifteenDaysAgo, "Completed at service center", true))
        
        // Sample trips
        trips.add(Trip(1, today, 125.3, 95.5, 78.6))
        trips.add(Trip(2, sevenDaysAgo, 85.7, 65.2, 79.0))
        trips.add(Trip(3, fourteenDaysAgo, 156.2, 120.8, 77.5))
        trips.add(Trip(4, twentyOneDaysAgo, 45.6, 35.0, 78.2))
    }
    
    // Fuel Entry methods
    suspend fun getAllFuelEntries(): List<FuelEntry> {
        return fuelEntries.sortedByDescending { it.date }
    }
    
    suspend fun getRecentFuelEntries(limit: Int): List<FuelEntry> {
        return fuelEntries.sortedByDescending { it.date }.take(limit)
    }
    
    suspend fun insertFuelEntry(fuelEntry: FuelEntry) {
        val newId = (fuelEntries.maxOfOrNull { it.id } ?: 0) + 1
        fuelEntries.add(fuelEntry.copy(id = newId))
    }
    
    suspend fun deleteFuelEntry(fuelEntry: FuelEntry) {
        fuelEntries.removeIf { it.id == fuelEntry.id }
    }
    
    // Maintenance Task methods
    suspend fun getAllMaintenanceTasks(): List<MaintenanceTask> {
        return maintenanceTasks.sortedBy { it.dueDate }
    }
    
    suspend fun getUpcomingMaintenanceTasks(limit: Int = Int.MAX_VALUE): List<MaintenanceTask> {
        return maintenanceTasks
            .filter { !it.isCompleted }
            .sortedBy { it.dueDate }
            .take(limit)
    }
    
    suspend fun getCompletedMaintenanceTasks(): List<MaintenanceTask> {
        return maintenanceTasks
            .filter { it.isCompleted }
            .sortedByDescending { it.dueDate }
    }
    
    suspend fun insertMaintenanceTask(task: MaintenanceTask) {
        val newId = (maintenanceTasks.maxOfOrNull { it.id } ?: 0) + 1
        maintenanceTasks.add(task.copy(id = newId))
    }
    
    suspend fun updateMaintenanceTask(task: MaintenanceTask) {
        val index = maintenanceTasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            maintenanceTasks[index] = task
        }
    }
    
    suspend fun deleteMaintenanceTask(task: MaintenanceTask) {
        maintenanceTasks.removeIf { it.id == task.id }
    }
    
    suspend fun getTotalMaintenanceTasks(): Int {
        return maintenanceTasks.size
    }
    
    suspend fun getCompletedMaintenanceTasksCount(): Int {
        return maintenanceTasks.count { it.isCompleted }
    }
    
    // Trip methods
    suspend fun getAllTrips(): List<Trip> {
        return trips.sortedByDescending { it.date }
    }
    
    suspend fun getRecentTrips(limit: Int): List<Trip> {
        return trips.sortedByDescending { it.date }.take(limit)
    }
    
    suspend fun insertTrip(trip: Trip) {
        val newId = (trips.maxOfOrNull { it.id } ?: 0) + 1
        trips.add(trip.copy(id = newId))
    }
    
    // Vehicle Health methods
    suspend fun getFuelEfficiencyData(): List<Pair<Long, Double>> {
        // Calculate fuel efficiency from consecutive fuel entries
        val result = mutableListOf<Pair<Long, Double>>()
        
        if (fuelEntries.size >= 2) {
            val sortedEntries = fuelEntries.sortedBy { it.odometer }
            
            for (i in 1 until sortedEntries.size) {
                val current = sortedEntries[i]
                val previous = sortedEntries[i-1]
                
                val distanceTraveled = (current.odometer - previous.odometer).toDouble()
                val fuelUsed = current.amount.toDouble()
                
                if (distanceTraveled > 0 && fuelUsed > 0) {
                    val efficiency = distanceTraveled / fuelUsed
                    result.add(Pair(current.date.time, efficiency))
                }
            }
        }
        
        return result.sortedBy { it.first }
    }
    
    companion object {
        @Volatile
        private var instance: AppRepository? = null
        
        fun getInstance(): AppRepository {
            return instance ?: synchronized(this) {
                instance ?: AppRepository().also { instance = it }
            }
        }
    }
}
