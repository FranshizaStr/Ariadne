package com.franshizastr.ariadne.di

import android.content.Context
import com.franshizastr.core.di.DispatchersModule
import com.franshizastr.login.di.LoginDataModule
import com.franshizastr.login.di.LoginRepositoryModule
import com.franshizastr.login.di.LoginDeps
import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.records.di.GPSRepositoryModule
import com.franshizastr.records.di.RecordsDataModule
import com.franshizastr.records.di.RecordsDeps
import com.franshizastr.records.di.RecordsRepositoryModule
import com.franshizastr.records.repositories.CurrentGpsRepository
import com.franshizastr.records.repositories.RecordRepository
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        LoginDataModule::class,
        LoginRepositoryModule::class,
        DispatchersModule::class,
        RecordsDataModule::class,
        RecordsRepositoryModule::class,
        GPSRepositoryModule::class
    ]
)
interface AriadneAppComponent : LoginDeps, RecordsDeps {

    override fun teamsRepository(): TeamsRepository

    override fun recordsRepository(): RecordRepository

    override fun gpsRepository(): CurrentGpsRepository

    override fun context(): Context

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AriadneAppComponent
    }
}

