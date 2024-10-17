package com.franshizastr.login.usecases

import com.franshizastr.login.models.TeamModel
import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.CleanResult
import java.util.UUID
import javax.inject.Inject

class AddTeamUseCase @Inject constructor(private val repo: TeamsRepository) {
    suspend fun execute(teamName: String): CleanResult<String> {
        val id = UUID.randomUUID().toString()
        val model = TeamModel(
            id = id,
            teamName = teamName
        )
        val result = repo.addTeam(model)
        return result.unwrapWithCallbacks(
            onSuccess = { _ -> CleanResult.Success(id) },
            onError = { res -> res }
        )
    }
}
