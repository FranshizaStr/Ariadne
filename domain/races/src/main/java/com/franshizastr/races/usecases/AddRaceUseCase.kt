package com.franshizastr.races.usecases

import com.franshizastr.CleanResult
import com.franshizastr.races.models.RaceModel
import com.franshizastr.races.repository.RacesRepository
import java.util.UUID
import javax.inject.Inject

class AddRaceUseCase @Inject constructor(private val repo: RacesRepository) {
    suspend fun execute(
        raceName: String,
        teamId: String
    ): CleanResult<String> {
        val id = UUID.randomUUID().toString()
        val model = RaceModel(
            id = id,
            teamId = teamId,
            raceName = raceName
        )
        val result = repo.addRace(model)
        return result.unwrapWithCallbacks(
            onSuccess = { _, -> CleanResult.Success(id) },
            onError = { res -> res }
        )
    }
}