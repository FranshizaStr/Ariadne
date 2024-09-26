package com.franshizastr.records.di

import android.content.Context
import com.franshizastr.core.di.DispatchersModule
import com.franshizastr.records.usecases.GetCSVFileNameForTeamUseCase
import com.franshizastr.records.repositories.CurrentGpsRepository
import com.franshizastr.records.repositories.RecordRepository
import com.franshizastr.records.usecases.GetAllRecordsByTeamIdUseCase
import com.franshizastr.records.usecases.StartGpsRecordingUseCase
import com.franshizastr.records.usecases.RemoveTeamRecordsUseCase
import com.franshizastr.records.usecases.StopGpsRecordingUseCase
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
        startGpsRecordingUseCase: StartGpsRecordingUseCase,
        writeFinalFileUseCase: WriteFinalFileUseCase,
        removeTeamRecordsUseCase: RemoveTeamRecordsUseCase,
        stopGpsRecordingUseCase: StopGpsRecordingUseCase,
        @Named("teamId") teamId: String,
        @Named("teamName") teamName: String
    ): RecordsViewModel {
        return RecordsViewModel(
            teamId,
            teamName,
            getCSVFileNameForTeamUseCase,
            getAllRecordsByTeamIdUseCase,
            startGpsRecordingUseCase,
            writeFinalFileUseCase,
            removeTeamRecordsUseCase,
            stopGpsRecordingUseCase
        )
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class RecordsPresentationScope