package com.franshizastr.records.usecases

import com.franshizastr.CleanResult
import com.franshizastr.records.models.map
import com.franshizastr.records.sharedPrefs.RecordingStatusSharedPref

import javax.inject.Inject

class StartGpsRecordingUseCase @Inject constructor(
    private val getGpsUseCase: GetCurrentGpsPointUseCase,
    private val saveRecordUseCase: SaveRecordUseCase,
    private val recordingStatusSharedPref: RecordingStatusSharedPref
) {
    suspend fun execute(
        sendMessageOnNewLocation: (message: String) -> Unit
    ): CleanResult<Unit> {
        return getGpsUseCase.execute { result ->
            val recordingStatus = recordingStatusSharedPref.getStatus()
            if (recordingStatus is RecordingStatusSharedPref.RecordingStatus.TeamIsRecorded) {
                val lastLocation = result.lastLocation
                val record = lastLocation?.map(
                    teamId = recordingStatus.teamId,
                    raceId = recordingStatus.raceId
                )
                record?.let {
                    saveRecordUseCase.execute(record)
                    sendMessageOnNewLocation(record.toString())
                }
            }
        }
    }
}