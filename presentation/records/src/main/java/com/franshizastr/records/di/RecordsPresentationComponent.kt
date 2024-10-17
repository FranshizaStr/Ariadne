package com.franshizastr.records.di

import android.content.Context
import com.franshizastr.core.di.DispatchersModule
import com.franshizastr.core.contextInterfaces.AndroidServiceLauncherContextInterface
import com.franshizastr.records.usecases.GetCSVFileNameForTeamUseCase
import com.franshizastr.records.repositories.CurrentGpsRepository
import com.franshizastr.records.repositories.RecordRepository
import com.franshizastr.records.sharedPrefs.RecordingStatusSharedPref
import com.franshizastr.records.usecases.GetAllRecordsByTeamIdUseCase
import com.franshizastr.records.usecases.RemoveTeamRecordsUseCase
import com.franshizastr.records.usecases.WriteFinalFileUseCase
import com.franshizastr.records.viewModel.RecordsViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Scope

interface RecordsDeps {

    fun recordsRepository(): RecordRepository

    fun gpsRepository(): CurrentGpsRepository

    fun androidServiceLauncher(): AndroidServiceLauncherContextInterface

    fun recordingStatusSharedPref(): RecordingStatusSharedPref

    fun context(): Context
}

@RecordsPresentationScope
@Component(
    modules = [
        RecordsVMModule::class,
        DispatchersModule::class
    ],
    dependencies = [
        RecordsDeps::class
    ]
)
interface RecordsPresentationComponent {

    fun getRecordsViewModel(): RecordsViewModel

    @Component.Builder
    interface Builder {

        fun recordsDeps(recordsDeps: RecordsDeps): Builder

        @BindsInstance
        fun teamId(@Named("teamId") teamId: String): Builder

        @BindsInstance
        fun teamName(@Named("teamName") teamName: String): Builder

        @BindsInstance
        fun raceId(@Named("raceId") raceId: String): Builder

        @BindsInstance
        fun raceName(@Named("raceName") raceName: String): Builder

        fun build(): RecordsPresentationComponent
    }
}

@Module
class RecordsVMModule {

    @RecordsPresentationScope
    @Provides
    fun getRecordsViewModel(
        getCSVFileNameForTeamUseCase: GetCSVFileNameForTeamUseCase,
        getAllRecordsByTeamIdUseCase: GetAllRecordsByTeamIdUseCase,
        writeFinalFileUseCase: WriteFinalFileUseCase,
        removeTeamRecordsUseCase: RemoveTeamRecordsUseCase,
        androidServiceLauncher: AndroidServiceLauncherContextInterface,
        recordingStatusSharedPref: RecordingStatusSharedPref,
        @Named("teamId") teamId: String,
        @Named("teamName") teamName: String,
        @Named("raceId") raceId: String,
        @Named("raceName") raceName: String,
    ): RecordsViewModel {
        return RecordsViewModel(
            teamId = teamId,
            teamName = teamName,
            raceId = raceId,
            raceName = raceName,
            getCSVFileNameForTeamUseCase,
            getAllRecordsByTeamIdUseCase,
            writeFinalFileUseCase,
            removeTeamRecordsUseCase,
            androidServiceLauncher,
            recordingStatusSharedPref
        )
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class RecordsPresentationScope