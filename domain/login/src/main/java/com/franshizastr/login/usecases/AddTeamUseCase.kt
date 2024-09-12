package com.franshizastr.login.usecases

import com.franshizastr.login.models.TeamModel
import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.CleanResult
import java.util.UUID
import javax.inject.Inject

class AddTeamUseCase @Inject constructor(private val repo: TeamsRepository) {
    suspend fun execute(teamName: String): CleanResult<Unit> {
        val model = TeamModel(
            id = UUID.randomUUID().toString(),
            teamName = teamName
        )
        return repo.addTeam(model)
    }
}
