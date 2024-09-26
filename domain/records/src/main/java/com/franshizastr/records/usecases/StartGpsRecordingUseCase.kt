package com.franshizastr.records.usecases

import android.app.Activity
import com.franshizastr.CleanResult
import com.franshizastr.records.models.map
import javax.inject.Inject

class StartGpsRecordingUseCase @Inject constructor(
    private val getGpsUseCase: GetCurrentGpsPointUseCase,
    private val saveRecordUseCase: SaveRecordUseCase
) {
    suspend fun execute(
        teamId: String,
        activity: Activity,
    ): CleanResult<Unit> {
        return getGpsUseCase.execute(
            activity
        ) { result ->
            val lastLocation = result.lastLocation
            val record = lastLocation?.map(teamId)
            record?.let {
                saveRecordUseCase.execute(record)
            }
        }
    }
}