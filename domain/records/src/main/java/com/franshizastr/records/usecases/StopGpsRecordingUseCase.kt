package com.franshizastr.records.usecases

import com.franshizastr.records.repositories.CurrentGpsRepository
import com.franshizastr.records.sharedPrefs.RecordingStatusSharedPref
import javax.inject.Inject

class StopGpsRecordingUseCase @Inject constructor(
    private val repository: CurrentGpsRepository,
    private val recordingStatusSharedPref: RecordingStatusSharedPref
) {
    suspend fun execute() {
        recordingStatusSharedPref.changeStatus(
            RecordingStatusSharedPref.RecordingStatus.NoTeamIsRecorded
        )
        repository.stopGpsRecording()
    }
}