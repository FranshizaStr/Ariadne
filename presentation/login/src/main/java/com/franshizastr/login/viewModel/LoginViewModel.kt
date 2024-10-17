package com.franshizastr.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franshizastr.CleanResult
import com.franshizastr.ErrorVO
import com.franshizastr.login.models.map
import com.franshizastr.login.models.LoginScreenEvent
import com.franshizastr.login.models.LoginState
import com.franshizastr.login.usecases.AddTeamUseCase
import com.franshizastr.login.usecases.EditTeamUseCase
import com.franshizastr.login.usecases.GetAllTeamsUseCase
import com.franshizastr.map
import com.franshizastr.toErrorVO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    getAllTeamsUseCase: GetAllTeamsUseCase,
    private val addTeamUseCase: AddTeamUseCase,
    private val editTeamUseCase: EditTeamUseCase,
    private val onTeamSnippetClick: (teamId: String, teamName: String) -> (Unit)
) : ViewModel() {

    private val _error = MutableStateFlow<ErrorVO?>(null)
    private val _state = getAllTeamsUseCase
        .execute()
        .unwrapWithCallbacks(
            onSuccess = { result ->
                result.map { models ->
                    val teamVos = models.map { model -> model.map() }
                    LoginState(teams = teamVos)
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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LoginState())

    val state = combine(_state, _error) { state, error ->
        state.copy(error = error)
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), LoginState())

    fun onEvent(event: LoginScreenEvent) {
        when(event) {
            is LoginScreenEvent.LongTapOnTeamEvent -> {
                viewModelScope.launch {
                    if (event.teamName.isEmpty() || event.teamName.isBlank()) {
                        _error.emit(
                            ("название команды не может быть пустым").toErrorVO()
                        )
                        return@launch
                    }
                    editTeamUseCase.execute(
                        id = event.teamId,
                        teamName = event.teamName
                    )
                }
            }
            is LoginScreenEvent.OnNewTeamSelectEvent -> {
                viewModelScope.launch {
                    if (event.teamName.isEmpty() || event.teamName.isBlank()) {
                        _error.emit(
                            ("название команды не может быть пустым").toErrorVO()
                        )
                        return@launch
                    }
                    val teamIdResult = async { addTeamUseCase.execute(event.teamName) }.await()
                    when(teamIdResult) {
                        is CleanResult.Success -> {
                            onTeamSnippetClick(teamIdResult.value, event.teamName, )
                        }
                        is CleanResult.Failure -> {
                            _error.emit(
                                teamIdResult.error.map()
                            )
                        }
                    }
                }
            }
            is LoginScreenEvent.OnOldTeamSelectEvent -> {
                onTeamSnippetClick(event.teamId, event.teamName)
            }
            is LoginScreenEvent.OnErrorEventShown -> {
                viewModelScope.launch {
                    _error.emit(null)
                }
            }
        }
    }
}