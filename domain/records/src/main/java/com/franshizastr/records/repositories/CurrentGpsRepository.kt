package com.franshizastr.records.repositories

import android.location.Location
import com.franshizastr.CleanResult

interface CurrentGpsRepository {

    suspend fun getCurrentGps(): CleanResult<Location>
}