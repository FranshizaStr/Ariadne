package com.franshizastr.races.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franshizastr.CleanResult
import com.franshizastr.ErrorVO
import com.franshizastr.map
import com.franshizastr.races.models.RaceScreenEvent
import com.franshizastr.races.models.RacesState
import com.franshizastr.races.models.map
import com.franshizastr.races.usecases.AddRaceUseCase
import com.franshizastr.races.usecases.EditRaceUseCase
import com.franshizastr.races.usecases.GetAllTeamRacesUseCase
import com.franshizastr.toErrorVO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RacesViewModel(
    getAllTeamRacesUseCase: GetAllTeamRacesUseCase,
    private val teamId: String,
    private val teamName: String,
    private val addRaceUseCase: AddRaceUseCase,
    private val editRaceUseCase: EditRaceUseCase,
    private val onRaceSnippetClick: (teamId: String, teamName: String, raceId: String, raceName: String) -> Unit
) : ViewModel() {

    private val _error = MutableStateFlow<ErrorVO?>(null)
    private val _state = getAllTeamRacesUseCase
        .execute(teamId)
        .unwrapWithCallbacks(
            onSuccess = { result ->
                result.map { models ->
                    val raceVos = models.map { model ->
                        model.map()
                    }
                    RacesState(raceVos)
                }
            },
            onError = { result ->
                flow {
                    result.error.message?.let { errorMessage ->
                        _error.emit(
                            ErrorVO(errorMessage)
                        )
                    }
                }
            }
        )

    val state = combine(_state, _error) { state, error ->
        state.copy(error = error)
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), RacesState())

    fun onEvent(event: RaceScreenEvent) {
        when(event) {
            is RaceScreenEvent.LongTapOnRaceEvent -> {
                viewModelScope.launch {
                    if (event.raceName.isEmpty() || event.raceName.isBlank()) {
                        _error.emit(
                            ("название команды не может быть пустым").toErrorVO()
                        )
                        return@launch
                    }
                    editRaceUseCase.execute(
                        model = event.raceModel,
                        raceName = event.raceName,
                    )
                }
            }
            is RaceScreenEvent.OnNewRaceSelectEvent -> {
                viewModelScope.launch {
                    if (event.raceName.isEmpty() || event.raceName.isBlank()) {
                        _error.emit(
                            ("название команды не может быть пустым").toErrorVO()
                        )
                        return@launch
                    }
                    val raceIdResult = addRaceUseCase.execute(
                        raceName = event.raceName,
                        teamId = teamId
                    )
                    when(raceIdResult) {
                        is CleanResult.Success -> {
                            onRaceSnippetClick(
                                teamId,
                                teamName,
                                raceIdResult.value,
                                event.raceName
                            )
                        }
                        is CleanResult.Failure -> {
                            _error.emit(
                                raceIdResult.error.map()
                            )
                        }
                    }
                }
            }
            is RaceScreenEvent.OnOldRaceSelectEvent -> {
                viewModelScope.launch {
                    onRaceSnippetClick(
                        teamId,
                        teamName,
                        event.raceId,
                        event.raceName
                    )
                }
            }
            is RaceScreenEvent.OnErrorEventShown -> {
                viewModelScope.launch {
                    viewModelScope.launch {
                        _error.emit(null)
                    }
                }
            }
        }
    }
}