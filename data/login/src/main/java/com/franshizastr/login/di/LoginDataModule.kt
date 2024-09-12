package com.franshizastr.login.di

import android.content.Context
import androidx.room.Room
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
    fun provideTeamsDatabase(context: Context): TeamsDatabase {
        return Room.databaseBuilder(
            context = context,
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

@Module(includes = [DispatchersModule::class])
interface LoginRepositoryModule {

    @Binds
    fun bindRepository(repositoryImpl: TeamRepositoryImpl): TeamsRepository
}