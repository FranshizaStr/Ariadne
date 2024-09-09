package com.franshizastr.login.repositories

import com.franshizastr.login.models.TeamModel
import com.franshizastr.errorsUtils.CleanResult

interface TeamsRepository {

    fun addTeam(model: TeamModel): CleanResult<Unit>

    fun getAllTeams(): CleanResult<List<TeamModel>>

    fun editTeam(id: String, teamName: String): CleanResult<Unit>

    fun removeTeams(ids: List<String>): CleanResult<Unit>

    fun selectTeam(id: String): CleanResult<Unit>
}
