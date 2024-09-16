package com.franshizastr.records.usecases

import com.franshizastr.CleanResult
import com.franshizastr.records.models.map
import javax.inject.Inject

class GetCurrentGpsAndSaveRecordUseCase @Inject constructor(
    private val getGpsUseCase: GetCurrentGpsPointUseCase,
    private val saveRecordUseCase: SaveRecordUseCase
) {
    suspend fun execute(teamId: String): CleanResult<Unit> {
        return when(val currentGpsResult = getGpsUseCase.execute()) {
            is CleanResult.Success -> {
                val location = currentGpsResult.value
                val record = location.map(teamId)
                saveRecordUseCase.execute(record)
                CleanResult.Success(Unit)
            }
            is CleanResult.Failure -> {
                currentGpsResult
            }
        }
    }
}