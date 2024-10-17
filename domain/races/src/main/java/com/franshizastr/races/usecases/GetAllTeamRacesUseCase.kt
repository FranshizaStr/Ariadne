package com.franshizastr.races.usecases

import com.franshizastr.CleanResult
import com.franshizastr.races.models.RaceModel
import com.franshizastr.races.repository.RacesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTeamRacesUseCase @Inject constructor(
    private val repository: RacesRepository
) {
    fun execute(teamId: String): CleanResult<Flow<List<RaceModel>>> {
        return repository.getAllRaceForTeam(teamId)
    }
}