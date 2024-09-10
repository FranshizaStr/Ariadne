package com.franshizastr.login.usecases

import com.franshizastr.login.models.TeamModel
import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.CleanResult
import javax.inject.Inject

class AddTeamUseCase @Inject constructor(private val repo: TeamsRepository) {
    fun execute(model: TeamModel): CleanResult<Unit> = repo.addTeam(model)
}
