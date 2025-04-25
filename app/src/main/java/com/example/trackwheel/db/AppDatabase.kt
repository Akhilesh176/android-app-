package com.example.trackwheel.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trackwheel.db.dao.FuelEntryDao
import com.example.trackwheel.db.dao.MaintenanceTaskDao
import com.example.trackwheel.db.dao.TripDao
import com.example.trackwheel.db.entities.FuelEntry
import com.example.trackwheel.db.entities.MaintenanceTask
import com.example.trackwheel.db.entities.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@Database(
    entities = [FuelEntry::class, MaintenanceTask::class, Trip::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun fuelEntryDao(): FuelEntryDao
    abstract fun maintenanceTaskDao(): MaintenanceTaskDao
    abstract fun tripDao(): TripDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trackwheel_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                populateDatabase(database)
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
        
        private suspend fun populateDatabase(database: AppDatabase) {
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
            
            // Sample fuel entries
            database.fuelEntryDao().insertFuelEntry(
                FuelEntry(date = today, amount = 45.5f, price = 65.75f, odometer = 12345)
            )
            database.fuelEntryDao().insertFuelEntry(
                FuelEntry(date = sevenDaysAgo, amount = 42.0f, price = 60.90f, odometer = 12100)
            )
            database.fuelEntryDao().insertFuelEntry(
                FuelEntry(date = fourteenDaysAgo, amount = 48.2f, price = 69.89f, odometer = 11850)
            )
            database.fuelEntryDao().insertFuelEntry(
                FuelEntry(date = twentyOneDaysAgo, amount = 40.8f, price = 59.16f, odometer = 11600)
            )
            
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
            
            database.maintenanceTaskDao().insertTask(
                MaintenanceTask(
                    description = "Oil Change",
                    dueDate = thirtyDaysLater,
                    notes = "Use synthetic oil 5W-30",
                    isCompleted = false
                )
            )
            database.maintenanceTaskDao().insertTask(
                MaintenanceTask(
                    description = "Tire Rotation",
                    dueDate = fifteenDaysLater,
                    notes = "Check tire pressure as well",
                    isCompleted = false
                )
            )
            database.maintenanceTaskDao().insertTask(
                MaintenanceTask(
                    description = "Air Filter Replacement",
                    dueDate = thirtyDaysLater,
                    notes = null,
                    isCompleted = false
                )
            )
            database.maintenanceTaskDao().insertTask(
                MaintenanceTask(
                    description = "Brake Inspection",
                    dueDate = fifteenDaysAgo,
                    notes = "Completed at service center",
                    isCompleted = true
                )
            )
            
            // Sample trips
            database.tripDao().insertTrip(
                Trip(date = today, distance = 125.3, duration = 95.5, averageSpeed = 78.6)
            )
            database.tripDao().insertTrip(
                Trip(date = sevenDaysAgo, distance = 85.7, duration = 65.2, averageSpeed = 79.0)
            )
            database.tripDao().insertTrip(
                Trip(date = fourteenDaysAgo, distance = 156.2, duration = 120.8, averageSpeed = 77.5)
            )
            database.tripDao().insertTrip(
                Trip(date = twentyOneDaysAgo, distance = 45.6, duration = 35.0, averageSpeed = 78.2)
            )
        }
    }
}
