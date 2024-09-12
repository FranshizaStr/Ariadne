package com.franshizastr.login.usecases

import com.franshizastr.CleanResult
import com.franshizastr.login.repositories.TeamsRepository
import javax.inject.Inject

class RemoveTeamsUseCase @Inject constructor(private val repo: TeamsRepository) {
    suspend fun execute(ids: List<String>): CleanResult<Unit> = repo.removeTeams(ids)
}
