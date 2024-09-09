package com.franshizastr.login.usecases

import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.errorsUtils.CleanResult

class EditTeamUseCase(private val repo: TeamsRepository) {
    fun execute(
        id: String,
        teamName: String
    ): CleanResult<Unit> {
        return repo.editTeam(id = id, teamName = teamName)
    }
}
