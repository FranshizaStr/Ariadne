package com.franshizastr.races.usecases

import com.franshizastr.CleanResult
import com.franshizastr.races.repository.RacesRepository
import javax.inject.Inject

class RemoveTeamRacesUseCase @Inject constructor(
    private val repository: RacesRepository
) {
    suspend fun execute(raceId: String): CleanResult<Unit> {
        return repository.removeTeamRaces(raceId)
    }
}