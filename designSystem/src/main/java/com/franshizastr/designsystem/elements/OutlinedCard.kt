package com.franshizastr.designsystem.elements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OutlinedCard(
    text: String,
    onClick: () -> Unit,
    focusManager: FocusManager,
    onLongTapFinish: (String) -> Unit,
) {
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    val isClickable = remember { mutableStateOf(false) }
    val inputText = remember { mutableStateOf(text) }
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
            value = inputText.value.uppercase(),
            onValueChange = { newValue -> inputText.value = newValue },
            enabled = isClickable.value,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
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
                    onLongTapFinish(inputText.value)
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
                        onLongTapFinish(inputText.value)
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
                            onLongTapFinish(inputText.value)
                        }
                    }
                }
        )
    }
}