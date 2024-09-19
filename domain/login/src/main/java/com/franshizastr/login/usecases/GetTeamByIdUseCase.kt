package com.franshizastr.login.usecases

import com.franshizastr.CleanResult
import com.franshizastr.login.models.TeamModel
import com.franshizastr.login.repositories.TeamsRepository
import javax.inject.Inject

class GetTeamByIdUseCase @Inject constructor(
    private val repository: TeamsRepository
) {
    fun execute(teamId: String): CleanResult<TeamModel> {
        return repository.getTeamById(teamId)
    }
}