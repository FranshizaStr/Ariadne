package com.franshizastr.login.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.franshizastr.designsystem.theme.AriadneTheme
import com.franshizastr.login.models.LoginScreenEvent
import com.franshizastr.login.models.TeamVO
import com.franshizastr.login.viewModel.LoginViewModel
import kotlinx.coroutines.delay
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
                item {
                    Title()
                }
                item {
                    NewTeamCard { teamName ->
                        viewModel.onEvent(
                            LoginScreenEvent.OnNewTeamSelectEvent(
                                teamName
                            )
                        )
                    }
                }
                items(state.teams) { team ->
                    OldTeamCard(
                        teamVO = team,
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

@Composable
internal fun Title() {
    val scope = LocalLifecycleOwner.current.lifecycleScope
    var animationProgress by remember { mutableStateOf("") }
    val text by remember { mutableStateOf("Выберите команду") }

    LaunchedEffect(animationProgress) {
        scope.launch {
            delay(1000L)
            when(animationProgress) {
                "" -> { animationProgress = "." }
                "." -> { animationProgress = ".." }
                ".." -> { animationProgress = "..." }
                "..." -> { animationProgress = "" }
            }
        }
    }
    Text(
        text = (text + animationProgress).uppercase(),
        fontSize = 22.sp,
        lineHeight = 26.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W700,
        letterSpacing = 0.1.em,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 30.dp)
            .padding(top = 30.dp),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NewTeamCard(
    onClickFinished: (text: String) -> Unit
) {
    val focusRequester = FocusRequester()
    val previousFocusStateWasActive = remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var isHintVisible by remember { mutableStateOf(true) }
    val borderColor = MaterialTheme.colorScheme.secondary

    val stroke = Stroke(
        width = 10f,
        pathEffect = PathEffect.
        chainPathEffect(
            outer = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f),
            inner = PathEffect.cornerPathEffect(50f)
        )
    )

    Row(
        horizontalArrangement = Arrangement.Absolute.Center,
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .padding(vertical = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .drawBehind {
                drawRoundRect(color = borderColor,style = stroke)
            }
            .combinedClickable(
                onClick = {
                    isHintVisible = false
                    focusRequester.requestFocus()
                },
                onDoubleClick = {  },
                onLongClick = {  }
            )
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newValue -> text = newValue.uppercase() },
            textStyle = TextStyle(
                fontSize = 26.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.W700,
                letterSpacing = 0.1.em
            ),
            decorationBox = { innerTextField ->
                if (isHintVisible) {
                    Text(
                        "введите новую команду\r\n+ для завершения создания\r\nкоманды нажмите галочку",
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable {
                                isHintVisible = false
                                focusRequester.requestFocus()
                            },
                        style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else {
                    innerTextField()
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onClickFinished(text) }
            ),
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(30.dp)
                .focusRequester(focusRequester)
                .onFocusEvent { event ->
                    when {
                        event.hasFocus -> previousFocusStateWasActive.value = true
                        previousFocusStateWasActive.value -> {
                            previousFocusStateWasActive.value = false
                            isHintVisible = true
                        }
                    }
                }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun OldTeamCard(
    teamVO: TeamVO,
    onClick: () -> Unit,
    focusManager: FocusManager,
    onLongTapFinish: (String) -> Unit,
) {
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    val isClickable = remember { mutableStateOf(false) }
    val text = remember { mutableStateOf(teamVO.teamName) }
    val previousFocusStateWasActive = remember { mutableStateOf(false) }
    val borderColor = MaterialTheme.colorScheme.secondary

    val stroke = Stroke(
        width = 10f,
        pathEffect = PathEffect.cornerPathEffect(50f)
    )

    LaunchedEffect(
        isClickable.value
    ) {
        focusRequester.requestFocus()
    }

    Row(
        horizontalArrangement = Arrangement.Absolute.Center,
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .padding(vertical = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .drawBehind {
                drawRoundRect(color = borderColor, style = stroke)
            }
            .combinedClickable(
                onLongClick = { isClickable.value = !(isClickable.value) },
                onDoubleClick = {  },
                onClick = { onClick() }
            )
    ) {
        BasicTextField(
            value = text.value.uppercase(),
            onValueChange = { newValue -> text.value = newValue },
            enabled = isClickable.value,
            textStyle = TextStyle(
                fontSize = 26.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.W700,
                letterSpacing = 0.1.em,
                color = MaterialTheme.colorScheme.tertiary
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onLongTapFinish(text.value)
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onLongTapFinish(text.value)
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            singleLine = true,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(30.dp)
                .focusRequester(focusRequester)
                .onKeyEvent { event ->
                    if (event.key == Key.Enter) {
                        onLongTapFinish(text.value)
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        return@onKeyEvent true
                    }
                    false
                }
                .onFocusEvent { event ->
                    when {
                        event.hasFocus -> previousFocusStateWasActive.value = true
                        previousFocusStateWasActive.value -> {
                            isClickable.value = false
                            previousFocusStateWasActive.value = false
                            onLongTapFinish(text.value)
                        }
                    }
                }
        )
    }
}
