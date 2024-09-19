package com.franshizastr.login.repositories

import com.franshizastr.login.models.TeamModel
import com.franshizastr.CleanResult
import com.franshizastr.login.database.TeamsDao
import com.franshizastr.login.database.map
import com.franshizastr.safelyExecuteDbOperation
import com.franshizastr.safelyExecuteSuspendableDbOperation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class TeamRepositoryImpl @Inject constructor(
    private val dao: TeamsDao,
    private val ioDispatcher: CoroutineDispatcher
) : TeamsRepository {

    override suspend fun addTeam(model: TeamModel): CleanResult<Unit> {
        return safelyExecuteSuspendableDbOperation(
            entity = model.map(),
            operationDispatcher = ioDispatcher,
            operationDescription = " adding new team to DB"
        ) { entity ->
            dao.insertTeam(entity!!)
            CleanResult.Success(Unit)
        }
    }

    override fun getAllTeams(): CleanResult<Flow<List<TeamModel>>> {
        return safelyExecuteDbOperation(
            entity = null,
            operationDescription = " observing teams from DB"
        ) { _ ->
            val teams = dao.getAllTeams().map { models ->
                models.map { model ->
                    model.map()
                }
            }
            CleanResult.Success(teams)
        }
    }

    override fun getTeamById(teamId: String): CleanResult<TeamModel> {
        return safelyExecuteDbOperation(
            entity = teamId,
            operationDescription = " getting team from DB",
        ) { id ->
            val result = dao.getTeamById(id!!).map()
            CleanResult.Success(result)
        }
    }

    override suspend fun editTeam(id: String, teamName: String): CleanResult<Unit> {
        return safelyExecuteSuspendableDbOperation(
            entity = TeamModel(id = id, teamName = teamName).map(),
            operationDispatcher = ioDispatcher,
            operationDescription = " editing teams in DB"
        ) { entity ->
            dao.updateTeam(entity!!)
            CleanResult.Success(Unit)
        }
    }

    override suspend fun removeTeams(ids: List<String>): CleanResult<Unit> {
        return safelyExecuteSuspendableDbOperation(
            entity = ids,
            operationDispatcher = ioDispatcher,
            operationDescription = " removing teams from DB"
        ) { entity ->
            dao.deleteTeam(entity!!)
            CleanResult.Success(Unit)
        }
    }
}