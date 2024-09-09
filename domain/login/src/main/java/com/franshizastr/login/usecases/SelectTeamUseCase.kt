package com.franshizastr.login.usecases

import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.errorsUtils.CleanResult

class SelectTeamUseCase(private val repo: TeamsRepository) {
    fun execute(id: String): CleanResult<Unit> = repo.selectTeam(id)
}
