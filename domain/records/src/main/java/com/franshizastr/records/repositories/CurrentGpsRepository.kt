package com.franshizastr.records.repositories

import com.franshizastr.CleanResult
import com.google.android.gms.location.LocationResult

typealias LocationResultCallback = suspend (locationResult: LocationResult) -> Unit

interface CurrentGpsRepository {

    suspend fun startGpsRecording(locationResultCallback: LocationResultCallback): CleanResult<Unit>

    suspend fun stopGpsRecording()
}