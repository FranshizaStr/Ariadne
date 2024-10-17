package com.franshizastr.records.repositories

import com.franshizastr.CleanResult
import com.franshizastr.records.models.RecordModel
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    fun getAllRecordsByTeamId(teamId: String, raceId: String): CleanResult<Flow<List<RecordModel>>>

    suspend fun removeTeamRecords(teamId: String, raceId: String): CleanResult<Unit>

    suspend fun saveRecord(record: RecordModel): CleanResult<Unit>
}