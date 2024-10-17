package com.franshizastr.races.usecases

import com.franshizastr.CleanResult
import com.franshizastr.races.models.RaceModel
import com.franshizastr.races.repository.RacesRepository
import javax.inject.Inject

class GetRaceByIdUseCase @Inject constructor(
    private val repository: RacesRepository
) {
    fun execute(raceId: String): CleanResult<RaceModel> {
        return repository.getRaceById(raceId)
    }
}