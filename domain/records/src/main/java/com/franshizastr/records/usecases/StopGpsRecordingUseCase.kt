package com.franshizastr.records.usecases

import com.franshizastr.records.repositories.CurrentGpsRepository
import javax.inject.Inject

class StopGpsRecordingUseCase @Inject constructor(
    val repository: CurrentGpsRepository
) {
    suspend fun execute() { repository.stopGpsRecording() }
}