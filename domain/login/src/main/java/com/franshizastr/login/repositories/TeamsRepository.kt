package com.franshizastr.login.repositories

import com.franshizastr.login.models.TeamModel
import com.franshizastr.CleanResult
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {

    suspend fun addTeam(model: TeamModel): CleanResult<Unit>

    suspend fun editTeam(id: String, teamName: String): CleanResult<Unit>

    suspend fun removeTeams(ids: List<String>): CleanResult<Unit>

    fun getAllTeams(): CleanResult<Flow<List<TeamModel>>>

    fun getTeamById(teamId: String): CleanResult<TeamModel>
}
