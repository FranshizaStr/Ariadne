package com.franshizastr.login.usecases

import com.franshizastr.login.models.TeamModel
import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.errorsUtils.CleanResult

class AddTeamUseCase(private val repo: TeamsRepository) {
    fun execute(model: TeamModel): CleanResult<Unit> = repo.addTeam(model)
}
