package com.franshizastr.records.usecases

import com.franshizastr.CleanResult
import com.franshizastr.records.models.RecordModel
import com.franshizastr.records.repositories.RecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRecordsByTeamIdUseCase @Inject constructor(
    private val repository: RecordRepository
) {

    fun execute(teamId: String, raceId: String): CleanResult<Flow<List<RecordModel>>> {
        return repository.getAllRecordsByTeamId(teamId, raceId)
    }
}