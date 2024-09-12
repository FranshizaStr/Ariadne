package com.franshizastr.login.usecases

import com.franshizastr.CleanResult
import com.franshizastr.login.repositories.TeamsRepository
import javax.inject.Inject

class EditTeamUseCase @Inject constructor(private val repo: TeamsRepository) {
    suspend fun execute(
        id: String,
        teamName: String
    ): CleanResult<Unit> {
        return repo.editTeam(id = id, teamName = teamName)
    }
}
