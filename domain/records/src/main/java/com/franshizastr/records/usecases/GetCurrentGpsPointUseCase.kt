package com.franshizastr.records.usecases

import com.franshizastr.CleanResult
import com.franshizastr.records.repositories.CurrentGpsRepository
import com.franshizastr.records.repositories.LocationResultCallback
import javax.inject.Inject

class GetCurrentGpsPointUseCase @Inject constructor(private val repository: CurrentGpsRepository) {

    suspend fun execute(
        locationResultCallback: LocationResultCallback
    ): CleanResult<Unit> {
        return repository.startGpsRecording(locationResultCallback)
    }
}