package com.franshizastr.races.di

import androidx.room.Room
import com.franshizastr.ContextProvider
import com.franshizastr.races.databases.RacesDao
import com.franshizastr.races.databases.RacesDatabase
import com.franshizastr.races.repositories.RacesRepositoryImpl
import com.franshizastr.races.repository.RacesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        RacesRepositoryModule::class
    ]
)
class RacesDomainModule {

    @Provides
    @Singleton
    fun provideRacesDatabase(contextProvider: ContextProvider): RacesDatabase {
        return Room.databaseBuilder(
            context = contextProvider.context,
            klass = RacesDatabase::class.java,
            name = "racesDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRacesDao(db: RacesDatabase): RacesDao {
        return db.dao
    }
}

@Module
interface RacesRepositoryModule {

    @Singleton
    @Binds
    fun bindRacesRepository(repository: RacesRepositoryImpl): RacesRepository
}