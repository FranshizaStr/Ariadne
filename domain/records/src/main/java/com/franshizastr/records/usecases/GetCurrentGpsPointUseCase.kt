package com.franshizastr.records.usecases

import android.app.Activity
import com.franshizastr.CleanResult
import com.franshizastr.records.repositories.CurrentGpsRepository
import com.franshizastr.records.repositories.LocationResultCallback
import javax.inject.Inject

class GetCurrentGpsPointUseCase @Inject constructor(private val repository: CurrentGpsRepository) {

    suspend fun execute(
        activity: Activity,
        locationResultCallback: LocationResultCallback
    ): CleanResult<Unit> {
        return repository.getCurrentGps(activity, locationResultCallback)
    }
}