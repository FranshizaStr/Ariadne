package com.franshizastr.races.repositories

import com.franshizastr.CleanResult
import com.franshizastr.races.databases.RacesDao
import com.franshizastr.races.databases.map
import com.franshizastr.races.models.RaceModel
import com.franshizastr.races.repository.RacesRepository
import com.franshizastr.safelyExecuteDbOperation
import com.franshizastr.safelyExecuteSuspendableDbOperation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RacesRepositoryImpl @Inject constructor(
    private val dao: RacesDao,
    private val ioDispatcher: CoroutineDispatcher
) : RacesRepository {

    override suspend fun addRace(model: RaceModel): CleanResult<Unit> {
        return safelyExecuteSuspendableDbOperation(
            entity = model.map(),
            operationDispatcher = ioDispatcher,
            operationDescription = " adding new race to DB"
        ) { entity ->
            dao.insertRace(entity!!)
            CleanResult.Success(Unit)
        }
    }

    override suspend fun editRace(model: RaceModel, raceName: String): CleanResult<Unit> {
        return safelyExecuteSuspendableDbOperation(
            entity = model.copy(raceName = raceName).map(),
            operationDispatcher = ioDispatcher,
            operationDescription = " editing race name"
        ) { entity ->
            dao.updateRace(entity!!)
            CleanResult.Success(Unit)
        }
    }

    override suspend fun removeTeamRaces(id: String): CleanResult<Unit> {
        return safelyExecuteSuspendableDbOperation(
            entity = id,
            operationDispatcher = ioDispatcher,
            operationDescription = " removing race from DB"
        ) { entity ->
            dao.deleteTeamRaces(entity!!)
            CleanResult.Success(Unit)
        }
    }

    override fun getAllRaceForTeam(teamId: String): CleanResult<Flow<List<RaceModel>>> {
        return safelyExecuteDbOperation(
            entity = teamId,
            operationDescription = " observing races by teamId",
        ) { entity ->
            val races = dao.getRacesByTeamId(entity!!).map { models ->
                models.map { model ->
                    model.map()
                }
            }
            CleanResult.Success(races)
        }
    }

    override fun getRaceById(raceId: String): CleanResult<RaceModel> {
        return safelyExecuteDbOperation(
            entity = raceId,
            operationDescription = " observing races from DB"
        ) { entity ->
            val race = dao.getRaceById(entity!!).map()
            CleanResult.Success(race)
        }
    }
}