package com.franshizastr.records.usecases

import com.franshizastr.CleanResult
import com.franshizastr.records.repositories.RecordRepository
import javax.inject.Inject

class RemoveTeamRecordsUseCase @Inject constructor(
    private val repository: RecordRepository
) {
    suspend fun execute(teamId: String): CleanResult<Unit> {
        return repository.removeTeamRecords(teamId)
    }
}