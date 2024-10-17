package com.franshizastr.ariadne.di

import android.content.Context
import com.franshizastr.ariadne.application.AriadneApplication
import com.franshizastr.core.di.DispatchersModule
import com.franshizastr.login.di.LoginDataModule
import com.franshizastr.login.di.LoginRepositoryModule
import com.franshizastr.login.di.LoginDeps
import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.core.contextInterfaces.AndroidServiceLauncherContextInterface
import com.franshizastr.races.di.RacesDeps
import com.franshizastr.races.di.RacesDomainModule
import com.franshizastr.races.repository.RacesRepository
import com.franshizastr.records.di.GPSRepositoryModule
import com.franshizastr.records.di.RecordsDataModule
import com.franshizastr.records.di.RecordsDeps
import com.franshizastr.records.di.RecordsRepositoryModule
import com.franshizastr.records.repositories.CurrentGpsRepository
import com.franshizastr.records.repositories.RecordRepository
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        LoginDataModule::class,
        LoginRepositoryModule::class,
        DispatchersModule::class,
        RecordsDataModule::class,
        RecordsRepositoryModule::class,
        GPSRepositoryModule::class,
        ServiceLauncherModule::class,
        ContextLauncher::class,
        RacesDomainModule::class
    ]
)
interface AriadneAppComponent : LoginDeps, RecordsDeps, LocationServiceDeps, RacesDeps {

    override fun teamsRepository(): TeamsRepository

    override fun recordsRepository(): RecordRepository

    override fun racesRepository(): RacesRepository

    override fun gpsRepository(): CurrentGpsRepository

    override fun androidServiceLauncher(): AndroidServiceLauncherContextInterface

    override fun context(): Context

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun ariadneApplication(application: AriadneApplication): Builder

        fun build(): AriadneAppComponent
    }
}

@Module
interface ServiceLauncherModule {

    @Binds
    fun bindAndroidServiceLauncher(
        application: AriadneApplication
    ): AndroidServiceLauncherContextInterface
}

@Module
interface ContextLauncher {

    @Binds
    fun bindContext(
        application: AriadneApplication
    ): Context
}
