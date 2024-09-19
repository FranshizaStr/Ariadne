package com.franshizastr.records.repositories

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.franshizastr.CleanResult
import com.franshizastr.ContextProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrentGpsRepositoryImpl @Inject constructor(
    private val contextProvider: ContextProvider,
    private val locationProvider: FusedLocationProviderClient,
    private val dispatcher: CoroutineDispatcher
): CurrentGpsRepository {

    private val defaultCancellationToken = object : CancellationToken() {
        override fun onCanceledRequested(p0: OnTokenCanceledListener) =
            CancellationTokenSource().token

        override fun isCancellationRequested() = false
    }

    override suspend fun getCurrentGps(): CleanResult<Location> {
        val context = contextProvider.context
        val fineLocationPermissionStatus = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val isFineLocationPermissionGranted =
            fineLocationPermissionStatus == PackageManager.PERMISSION_GRANTED
        val coarseLocationPermissionStatus = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val isCoarsePermissionGranted =
            coarseLocationPermissionStatus == PackageManager.PERMISSION_GRANTED

        return if (isFineLocationPermissionGranted && isCoarsePermissionGranted) {
            val locationTask = locationProvider.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                defaultCancellationToken
            )
            withContext(dispatcher) {
                try {
                    val location = Tasks.await(locationTask)
                    CleanResult.Success(location)
                } catch (ex: Throwable) {
                    CleanResult.Failure(
                        error = CleanResult.Error(
                            previousError = null,
                            throwable = ex,
                            level = CleanResult.Error.ErrorLevel.DATA,
                            message = "error happened while retrieving current user location"
                        )
                    )
                }
            }
        } else {
            CleanResult.Failure(
                error = CleanResult.Error(
                    previousError = null,
                    throwable = null,
                    level = CleanResult.Error.ErrorLevel.DATA,
                    message = "location permissions not granted"
                )
            )
        }
    }
}
