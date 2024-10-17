package com.franshizastr.login.ui

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
import com.franshizastr.login.models.LoginScreenEvent
import com.franshizastr.login.viewModel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
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
                item { AnimatedTitle("Выберите команду") }
                item {
                    DottedOutlinedInputCardWithHint(
                        hintText = "введите новую команду\r\n+ " +
                                "для завершения создания\r\nкоманды нажмите галочку"
                    ) { teamName ->
                        viewModel.onEvent(
                            LoginScreenEvent.OnNewTeamSelectEvent(
                                teamName
                            )
                        )
                    }
                }
                items(state.teams) { team ->
                    OutlinedCard(
                        text = team.teamName,
                        onClick = {
                            viewModel.onEvent(
                                LoginScreenEvent.OnOldTeamSelectEvent(team.id, team.teamName)
                            )
                        },
                        focusManager = focusManager,
                        onLongTapFinish = { newName ->
                            viewModel.onEvent(
                                LoginScreenEvent.LongTapOnTeamEvent(
                                    teamName = newName,
                                    teamId = team.id
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
                    viewModel.onEvent(LoginScreenEvent.OnErrorEventShown)
                }
            }
        }
    }
}
