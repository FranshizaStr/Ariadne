package com.franshizastr.records.repositories

import android.app.Activity
import com.franshizastr.CleanResult
import com.google.android.gms.location.LocationResult

typealias LocationResultCallback = suspend (locationResult: LocationResult) -> Unit

interface CurrentGpsRepository {

    suspend fun getCurrentGps(
        activity: Activity,
        locationResultCallback: LocationResultCallback
    ): CleanResult<Unit>
}