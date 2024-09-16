package com.franshizastr.login.di

import androidx.room.Room
import com.franshizastr.ContextProvider
import com.franshizastr.core.di.DispatchersModule
import com.franshizastr.login.database.TeamsDao
import com.franshizastr.login.database.TeamsDatabase
import com.franshizastr.login.repositories.TeamRepositoryImpl
import com.franshizastr.login.repositories.TeamsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoginDataModule {

    @Provides
    @Singleton
    fun provideTeamsDatabase(contextProvider: ContextProvider): TeamsDatabase {
        return Room.databaseBuilder(
            context = contextProvider.context,
            klass = TeamsDatabase::class.java,
            name = "teamsDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTeamsDao(db: TeamsDatabase): TeamsDao {
        return db.dao
    }
}

@Module
interface LoginRepositoryModule {

    @Binds
    fun bindRepository(repositoryImpl: TeamRepositoryImpl): TeamsRepository
}