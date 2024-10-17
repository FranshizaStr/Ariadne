package com.franshizastr.records.usecases

import com.franshizastr.CleanResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCSVFileNameForTeamUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
) {

    suspend fun execute(teamId: String, teamName: String, raceName: String): CleanResult<String> {
        return withContext(dispatcher) {
            CleanResult.Success(
                value = "${raceName}_${teamName}_${teamId}"
            )
        }
    }
}