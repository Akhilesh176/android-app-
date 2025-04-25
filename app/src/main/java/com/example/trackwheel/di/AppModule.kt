package com.example.trackwheel.di

import android.content.Context
import com.example.trackwheel.db.AppDatabase
import com.example.trackwheel.db.dao.FuelEntryDao
import com.example.trackwheel.db.dao.MaintenanceTaskDao
import com.example.trackwheel.db.dao.TripDao
import com.example.trackwheel.repositories.FuelRepository
import com.example.trackwheel.repositories.MaintenanceRepository
import com.example.trackwheel.repositories.TripRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideFuelEntryDao(database: AppDatabase): FuelEntryDao {
        return database.fuelEntryDao()
    }
    
    @Provides
    fun provideMaintenanceTaskDao(database: AppDatabase): MaintenanceTaskDao {
        return database.maintenanceTaskDao()
    }
    
    @Provides
    fun provideTripDao(database: AppDatabase): TripDao {
        return database.tripDao()
    }
    
    @Provides
    @Singleton
    fun provideFuelRepository(fuelEntryDao: FuelEntryDao): FuelRepository {
        return FuelRepository(fuelEntryDao)
    }
    
    @Provides
    @Singleton
    fun provideMaintenanceRepository(maintenanceTaskDao: MaintenanceTaskDao): MaintenanceRepository {
        return MaintenanceRepository(maintenanceTaskDao)
    }
    
    @Provides
    @Singleton
    fun provideTripRepository(tripDao: TripDao): TripRepository {
        return TripRepository(tripDao)
    }
}
