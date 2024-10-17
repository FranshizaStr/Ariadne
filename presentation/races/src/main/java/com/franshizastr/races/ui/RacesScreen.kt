package com.franshizastr.races.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.franshizastr.designsystem.elements.AnimatedTitle
import com.franshizastr.designsystem.elements.DottedOutlinedInputCardWithHint
import com.franshizastr.designsystem.elements.OutlinedCard
import com.franshizastr.designsystem.theme.AriadneTheme
import com.franshizastr.races.models.RaceScreenEvent
import com.franshizastr.races.models.map
import com.franshizastr.races.viewModel.RacesViewModel
import kotlinx.coroutines.launch

@Composable
fun RacesScreen(
    viewModel: RacesViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val hostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    AriadneTheme {

        Scaffold(
            snackbarHost = { SnackbarHost(hostState) },
        ) { _ ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { focusManager.clearFocus() }
                    ),
                verticalArrangement = Arrangement.Center,
            ) {
                item { AnimatedTitle("Выберите гонку") }
                item {
                    DottedOutlinedInputCardWithHint(
                        hintText = "введите новую гонку\r\n+ " +
                                "для завершения создания\r\nгонки нажмите галочку"
                    ) { raceName ->
                        viewModel.onEvent(
                            RaceScreenEvent.OnNewRaceSelectEvent(
                                raceName
                            )
                        )
                    }
                }
                items(state.races) { race ->
                    OutlinedCard(
                        text = race.raceName,
                        onClick = {
                            viewModel.onEvent(
                                RaceScreenEvent.OnOldRaceSelectEvent(race.id, race.raceName)
                            )
                        },
                        focusManager = focusManager,
                        onLongTapFinish = { raceName ->
                            viewModel.onEvent(
                                RaceScreenEvent.LongTapOnRaceEvent(
                                    raceModel = race.map(),
                                    raceName = raceName
                                )
                            )
                        },
                    )
                }
            }

            val error = state.error

            LaunchedEffect(error?.value) {
                coroutineScope.launch {
                    error?.value?.let { errorText ->
                        hostState
                            .showSnackbar(errorText)
                    }
                    viewModel.onEvent(RaceScreenEvent.OnErrorEventShown)
                }
            }
        }
    }
}
