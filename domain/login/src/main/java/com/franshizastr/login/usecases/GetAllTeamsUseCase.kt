package com.franshizastr.login.usecases

import com.franshizastr.CleanResult
import com.franshizastr.login.models.TeamModel
import com.franshizastr.login.repositories.TeamsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTeamsUseCase @Inject constructor(private val repo: TeamsRepository) {
    fun execute(): CleanResult<Flow<List<TeamModel>>> = repo.getAllTeams()
}
