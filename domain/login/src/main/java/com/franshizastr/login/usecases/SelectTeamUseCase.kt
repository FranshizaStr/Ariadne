package com.franshizastr.login.usecases

import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.CleanResult
import javax.inject.Inject

class SelectTeamUseCase @Inject constructor(private val repo: TeamsRepository) {
    fun execute(id: String): CleanResult<Unit> = repo.selectTeam(id)
}
