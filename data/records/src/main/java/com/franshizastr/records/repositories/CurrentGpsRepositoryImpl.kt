package com.franshizastr.records.repositories

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.franshizastr.CleanResult
import com.franshizastr.ContextProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrentGpsRepositoryImpl @Inject constructor(
    private val contextProvider: ContextProvider,
    private val locationProvider: FusedLocationProviderClient,
    private val dispatcher: CoroutineDispatcher,
): CurrentGpsRepository {

    private var locationCoroutineScope: CoroutineScope? = CoroutineScope(dispatcher)
    private var locationCallback: LocationCallback? = null

    override suspend fun startGpsRecording(
        locationResultCallback: LocationResultCallback
    ): CleanResult<Unit> {

        // проверяем разрешения
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

        // обновляем колбэк и скоуп для корутины
        locationCoroutineScope?.cancel()
        locationCoroutineScope = CoroutineScope(dispatcher)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                locationCoroutineScope?.launch {
                    locationResultCallback(p0)
                }
                super.onLocationResult(p0)
            }
        }


        return if (isFineLocationPermissionGranted && isCoarsePermissionGranted) {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                3000L
            )
                .setMinUpdateIntervalMillis(2000L)
                .build()
            val locationSettingsBuilder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            val locationSettingClient =
                LocationServices.getSettingsClient(context)
            val locationSettingsTask =
                locationSettingClient.checkLocationSettings(locationSettingsBuilder.build())
            withContext(dispatcher) {
                try {
                    Tasks.await(locationSettingsTask)
                    locationCallback?.let { callback ->
                        locationProvider.requestLocationUpdates(
                            locationRequest,
                            callback,
                            Looper.getMainLooper()
                        )
                        CleanResult.Success(Unit)
                    } ?: CleanResult.Failure(
                        error = CleanResult.Error(
                            previousError = null,
                            throwable = null,
                            level = CleanResult.Error.ErrorLevel.DATA,
                            message = "there was no location callback"
                        )
                    )
                } catch (ex: Exception) {
                    val locationSettingsError = CleanResult.Error(
                        previousError = null,
                        throwable = ex,
                        level = CleanResult.Error.ErrorLevel.DATA,
                        message = "error happened while retrieving current user location"
                    )
                    CleanResult.Failure(
                        error = locationSettingsError
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

    override suspend fun stopGpsRecording() {
        locationCoroutineScope?.cancel()
        locationCoroutineScope = null
        locationCallback?.let { callback ->
            withContext(dispatcher) {
                val stopTask = locationProvider.removeLocationUpdates(callback)
                Tasks.await(stopTask)
            }
        }
        locationCallback = null
    }

//    companion object {
//        private const val REQUEST_CHECK_SETTINGS = 1
//    }
}
