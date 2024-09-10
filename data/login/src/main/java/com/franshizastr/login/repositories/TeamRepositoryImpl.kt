package com.franshizastr.login.repositories

import com.franshizastr.login.models.TeamModel
import com.franshizastr.CleanResult

class TeamRepositoryImpl : TeamsRepository {

    override fun addTeam(model: TeamModel): CleanResult<Unit> {
        TODO("Not yet implemented")
    }

    override fun getAllTeams(): CleanResult<List<TeamModel>> {
        TODO("Not yet implemented")
    }

    override fun editTeam(id: String, teamName: String): CleanResult<Unit> {
        TODO("Not yet implemented")
    }

    override fun removeTeams(ids: List<String>): CleanResult<Unit> {
        TODO("Not yet implemented")
    }

    override fun selectTeam(id: String): CleanResult<Unit> {
        TODO("Not yet implemented")
    }


}