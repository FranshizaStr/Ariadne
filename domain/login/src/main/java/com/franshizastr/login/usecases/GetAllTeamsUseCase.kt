package com.franshizastr.login.usecases

import com.franshizastr.login.models.TeamModel
import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.errorsUtils.CleanResult

class GetAllTeamsUseCase(private val repo: TeamsRepository) {
    fun execute(): CleanResult<List<TeamModel>> = repo.getAllTeams()
}
