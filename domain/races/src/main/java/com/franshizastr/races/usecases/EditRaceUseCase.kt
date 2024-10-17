package com.franshizastr.races.usecases

import com.franshizastr.CleanResult
import com.franshizastr.races.models.RaceModel
import com.franshizastr.races.repository.RacesRepository
import javax.inject.Inject

class EditRaceUseCase @Inject constructor(private val repo: RacesRepository) {
    suspend fun execute(
        model: RaceModel,
        raceName: String
    ): CleanResult<Unit> {
        return repo.editRace(model = model, raceName = raceName)
    }
}