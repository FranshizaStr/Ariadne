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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DottedOutlinedInputCardWithHint(
    hintText: String,
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
            cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
            textStyle = TextStyle(
                fontSize = 26.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.W700,
                letterSpacing = 0.1.em,
                color = MaterialTheme.colorScheme.tertiary,
            ),
            decorationBox = { innerTextField ->
                if (isHintVisible) {
                    Text(
                        text = hintText,
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
