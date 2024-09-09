package com.franshizastr.login.usecases

import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.errorsUtils.CleanResult

class RemoveTeamsUseCase(private val repo: TeamsRepository) {
    fun execute(ids: List<String>): CleanResult<Unit> = repo.removeTeams(ids)
}
