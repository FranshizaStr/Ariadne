package com.franshizastr.records.di

import androidx.room.Room
import com.franshizastr.ContextProvider
import com.franshizastr.records.database.RecordsDao
import com.franshizastr.records.database.RecordsDatabase
import com.franshizastr.records.repositories.CurrentGpsRepository
import com.franshizastr.records.repositories.CurrentGpsRepositoryImpl
import com.franshizastr.records.repositories.RecordRepository
import com.franshizastr.records.repositories.RecordRepositoryImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        RecordsRepositoryModule::class,
        GPSRepositoryModule::class
    ]
)
class RecordsDataModule {

    @Provides
    @Singleton
    fun provideRecordsDatabase(contextProvider: ContextProvider): RecordsDatabase {
        return Room.databaseBuilder(
            context = contextProvider.context,
            klass = RecordsDatabase::class.java,
            name = "recordsDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecordsDao(db: RecordsDatabase): RecordsDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun provideLocationProvider(contextProvider: ContextProvider): FusedLocationProviderClient {
        val ctx = contextProvider.context
        val locationProvider = LocationServices.getFusedLocationProviderClient(ctx)
        return locationProvider
    }
}

@Module
interface RecordsRepositoryModule {

    @Singleton
    @Binds
    fun bindRecordsRepository(repository: RecordRepositoryImpl): RecordRepository
}

@Module
interface GPSRepositoryModule {

    @Singleton
    @Binds
    fun bindGPSRepository(repository: CurrentGpsRepositoryImpl): CurrentGpsRepository
}