package com.franshizastr.records.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franshizastr.CleanResult
import com.franshizastr.ErrorVO
import com.franshizastr.records.usecases.GetCSVFileNameForTeamUseCase
import com.franshizastr.map
import com.franshizastr.records.models.RecordsScreenEvent
import com.franshizastr.records.models.RecordsState
import com.franshizastr.records.models.map
import com.franshizastr.records.usecases.GetAllRecordsByTeamIdUseCase
import com.franshizastr.records.usecases.StartGpsRecordingUseCase
import com.franshizastr.records.usecases.RemoveTeamRecordsUseCase
import com.franshizastr.records.usecases.StopGpsRecordingUseCase
import com.franshizastr.records.usecases.WriteFinalFileUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecordsViewModel(
    private val teamId: String,
    private val teamName: String,
    private val getCSVFileNameForTeamUseCase: GetCSVFileNameForTeamUseCase,
    private val getAllRecordsByTeamIdUseCase: GetAllRecordsByTeamIdUseCase,
    private val startGpsRecordingUseCase: StartGpsRecordingUseCase,
    private val writeFinalFileUseCase: WriteFinalFileUseCase,
    private val removeTeamRecordsUseCase: RemoveTeamRecordsUseCase,
    private val stopGpsRecordingUseCase: StopGpsRecordingUseCase
) : ViewModel() {

    private val _error = MutableStateFlow<ErrorVO?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isRecording = MutableStateFlow(false)
    private val _state = getAllRecordsByTeamIdUseCase
        .execute(teamId)
        .unwrapWithCallbacks(
            onSuccess = { result ->
                result.map { models ->
                    val recordVos = models.map { model -> model.map() }
                    RecordsState(records = recordVos)
                }
            },
            onError = { result ->
                flow {
                    result.error.message?.let { errorMessage ->
                        _isLoading.emit(false)
                        _error.emit(ErrorVO(errorMessage))
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), RecordsState())
    val state = combine(_state, _error, _isLoading, _isRecording) { state, error, isLoading, isRecording ->
        state.copy(error = error, isLoading = isLoading, isRecording = isRecording)
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), RecordsState())

    fun onEvent(event: RecordsScreenEvent) {
        when(event) {
            is RecordsScreenEvent.ChangeRecordingStatus -> {
                viewModelScope.launch {
                    if (!_isRecording.value) {
                        _isLoading.emit(true)
                        val result = startGpsRecordingUseCase.execute(teamId, event.activity)
                        when (result) {
                            is CleanResult.Success -> _isRecording.emit(true)
                            is CleanResult.Failure -> {
                                _isRecording.emit(false)
                                _error.emit(
                                    result.error.map()
                                )
                            }
                        }
                    } else {
                        stopGpsRecordingUseCase.execute()
                        _isLoading.emit(false)
                        _isRecording.emit(false)
                    }
                }
            }
            is RecordsScreenEvent.SaveCSVFileWithRecords -> {
                viewModelScope.launch {
                    val nameDeferred = async { getCSVFileNameForTeamUseCase.execute(teamId, teamName) }
                    val recordsDeferred = async { getAllRecordsByTeamIdUseCase.execute(teamId) }
                    val nameResult = nameDeferred.await()
                    val recordsResult = recordsDeferred.await()
                    when {
                        nameResult is CleanResult.Success && recordsResult is CleanResult.Success -> {
                            val records = recordsResult.value
                                .stateIn(viewModelScope)
                                .value
                            val fileName = nameResult.value
                            writeFinalFileUseCase.execute(
                                records = records,
                                fileName = fileName,
                                fileWriter = event.fileWriter
                            )
                        }
                        nameResult is CleanResult.Failure -> {
                            _error.emit(
                                nameResult.error.map()
                            )
                        }
                        recordsResult is CleanResult.Failure -> {
                            _error.emit(
                                recordsResult.error.map()
                            )
                        }
                    }
                }
            }
            is RecordsScreenEvent.OnErrorEventShown -> {
                viewModelScope.launch {
                    _error.emit(null)
                }
            }
            is RecordsScreenEvent.DeleteRecords -> {
                viewModelScope.launch {
                    removeTeamRecordsUseCase.execute(teamId)
                }
            }
        }
    }
}