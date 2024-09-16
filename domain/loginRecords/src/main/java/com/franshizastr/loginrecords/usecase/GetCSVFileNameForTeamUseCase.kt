package com.franshizastr.loginrecords.usecase

import com.franshizastr.CleanResult
import com.franshizastr.login.usecases.GetTeamByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCSVFileNameForTeamUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val getTeamByIdUseCase: GetTeamByIdUseCase
) {

    suspend fun execute(teamId: String): CleanResult<String> {
        return withContext(dispatcher) {
            val team = async { getTeamByIdUseCase.execute(teamId) }.await()
            when(team) {
                is CleanResult.Success -> {
                    val teamName = team.valueOrNull?.teamName
                    CleanResult.Success(
                        value = "timeline_{${teamName}}_{$teamId}.csv"
                    )
                }
                is CleanResult.Failure -> CleanResult.Failure(
                    error = CleanResult.Error(
                        previousError = team.error,
                        level = CleanResult.Error.ErrorLevel.DOMAIN,
                        throwable = null,
                        message = "inner error"
                    )
                )
            }
        }
    }
}