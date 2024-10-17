package com.franshizastr.records.sharedPrefs

import kotlinx.coroutines.flow.StateFlow

interface RecordingStatusSharedPref {

    fun getStatus(): RecordingStatus

    suspend fun changeStatus(status: RecordingStatus)

    val statusFlow: StateFlow<RecordingStatus>

    sealed class RecordingStatus {
        data object NoTeamIsRecorded : RecordingStatus()
        data class TeamIsRecorded(
            val teamId: String,
            val teamName: String,
            val raceId: String
        ) : RecordingStatus()
    }
}