package com.franshizastr.ariadne.di

import com.franshizastr.ariadne.services.LocationService
import com.franshizastr.records.usecases.StartGpsRecordingUseCase
import com.franshizastr.records.usecases.StopGpsRecordingUseCase
import dagger.Component

interface LocationServiceDeps {

    fun startGpsRecordingUseCase(): StartGpsRecordingUseCase

    fun stopGpsRecordingUseCase(): StopGpsRecordingUseCase
}

@Component(
    dependencies = [
        LocationServiceDeps::class
    ]
)
interface LocationServiceComponent {

    @Component.Builder
    interface Builder {

        fun recordDeps(locationServiceDeps: LocationServiceDeps): Builder

        fun build(): LocationServiceComponent
    }

    fun inject(locationService: LocationService)
}