package com.franshizastr.records.sharedPrefs

import android.content.SharedPreferences
import com.franshizastr.records.sharedPrefs.RecordingStatusSharedPref.RecordingStatus
import com.franshizastr.records.sharedPrefs.RecordingStatusSharedPref.RecordingStatus.NoTeamIsRecorded
import com.franshizastr.records.sharedPrefs.RecordingStatusSharedPref.RecordingStatus.TeamIsRecorded
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class RecordingStatusSharedPrefImpl(
    private val sharedPreferences: SharedPreferences,
    private val dispatcher: CoroutineDispatcher
): RecordingStatusSharedPref,
    SharedPreferences by sharedPreferences {

    private val _statusFlow = MutableStateFlow(getStatus())
    override val statusFlow = _statusFlow.asStateFlow()

    override fun getStatus(): RecordingStatus {
        val teamId = sharedPreferences.getString(TEAM_ID_KEY, null)
        val teamName = sharedPreferences.getString(TEAM_ID_KEY, null)
        val raceId = sharedPreferences.getString(RACE_ID, null)
        return if (teamId != null && teamName != null && raceId != null) {
            TeamIsRecorded(
                teamId = teamId,
                teamName = teamName,
                raceId = raceId
            )
        } else {
            NoTeamIsRecorded
        }
    }

    override suspend fun changeStatus(
        status: RecordingStatus
    ) {
        val spEditor = sharedPreferences.edit()
        when(status) {
            is NoTeamIsRecorded -> {
                spEditor
                    .clear()
                    .apply()
            }
            is TeamIsRecorded -> {
                spEditor
                    .putString(TEAM_NAME_KEY, status.teamId)
                    .putString(TEAM_ID_KEY, status.teamId)
                    .putString(RACE_ID, status.raceId)
                    .apply()
            }
        }
        withContext(dispatcher) {
            _statusFlow.emit(status)
        }
    }

    companion object {
        const val NAME = "RecordingStatusSharedPref"
        private const val TEAM_NAME_KEY = "teamName"
        private const val TEAM_ID_KEY = "teamId"
        private const val RACE_ID = "raceId"
    }
}