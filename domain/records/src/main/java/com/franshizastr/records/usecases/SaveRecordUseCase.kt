package com.franshizastr.records.usecases

import com.franshizastr.CleanResult
import com.franshizastr.records.models.RecordModel
import com.franshizastr.records.repositories.RecordRepository
import javax.inject.Inject

class SaveRecordUseCase @Inject constructor(
    private val repository: RecordRepository,
) {

    suspend fun execute(record: RecordModel): CleanResult<Unit> {
        return repository.saveRecord(record)
    }
}