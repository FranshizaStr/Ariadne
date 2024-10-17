package com.franshizastr.races.repository

import com.franshizastr.CleanResult
import com.franshizastr.races.models.RaceModel
import kotlinx.coroutines.flow.Flow

interface RacesRepository {

    suspend fun addRace(model: RaceModel): CleanResult<Unit>

    suspend fun editRace(model: RaceModel, raceName: String): CleanResult<Unit>

    suspend fun removeTeamRaces(id: String): CleanResult<Unit>

    fun getAllRaceForTeam(teamId: String): CleanResult<Flow<List<RaceModel>>>

    fun getRaceById(raceId: String): CleanResult<RaceModel>
}