package com.franshizastr.records.usecases

import android.location.Location
import com.franshizastr.CleanResult
import com.franshizastr.records.repositories.CurrentGpsRepository
import javax.inject.Inject

class GetCurrentGpsPointUseCase @Inject constructor(
    private val repository: CurrentGpsRepository
) {

    suspend fun execute(): CleanResult<Location> {
        return repository.getCurrentGps()
    }
}