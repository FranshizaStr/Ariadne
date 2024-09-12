package com.franshizastr.login.repositories

import com.franshizastr.login.models.TeamModel
import com.franshizastr.CleanResult
import com.franshizastr.login.database.TeamsDao
import com.franshizastr.login.database.map
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class TeamRepositoryImpl @Inject constructor(
    private val dao: TeamsDao,
    @Named("IoDispatchers") private val ioDispatcher: CoroutineDispatcher
) : TeamsRepository {

    private suspend fun <T, S> safelyExecuteSuspendableDbOperation(
        entity: S?,
        operationDispatcher: CoroutineDispatcher,
        operationDescription: String,
        operation: suspend (teamsEntity: S?) -> CleanResult<T>,
    ): CleanResult<T> {
        return withContext(operationDispatcher) {
            try {
                operation(entity)
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                val error = CleanResult.Error(
                    previousError = null,
                    throwable = ex,
                    level = CleanResult.Error.ErrorLevel.DATA,
                    message = "exception happened while: $operationDescription"
                )
                CleanResult.Failure(
                    error = error
                )
            }
        }
    }

    private fun <T, S> safelyExecuteDbOperation(
        entity: S?,
        operationDescription: String,
        operation: (teamsEntity: S?) -> CleanResult<T>,
    ): CleanResult<T> {
        return try {
            operation(entity)
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Throwable) {
            val error = CleanResult.Error(
                previousError = null,
                throwable = ex,
                level = CleanResult.Error.ErrorLevel.DATA,
                message = "exception happened while: $operationDescription"
            )
            CleanResult.Failure(
                error = error
            )
        }
    }

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