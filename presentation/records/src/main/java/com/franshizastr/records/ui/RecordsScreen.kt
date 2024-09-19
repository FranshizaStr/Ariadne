package com.franshizastr.records.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.franshizastr.designsystem.Loading
import com.franshizastr.designsystem.theme.AriadneTheme
import com.franshizastr.records.models.RecordVO
import com.franshizastr.records.models.RecordsScreenEvent
import com.franshizastr.records.viewModel.RecordsViewModel
import kotlinx.coroutines.launch

@Composable
fun RecordsScreen(
    viewModel: RecordsViewModel,
    teamName: String
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val hostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    AriadneTheme {

        Scaffold(
            snackbarHost = { SnackbarHost(hostState) },
        ) { _ ->
            Column {
                TeamTitle(teamName)
                LazyColumn(
                    modifier = Modifier.weight(0.8f)
                ) {
                    items(
                        items = state.records,
                        key = { record -> record.id }
                    ) { record ->
                        val animatedModifier = Modifier.animateItem()
                        Record(record, animatedModifier)
                    }
                }
                Buttons(
                    viewModel = viewModel,
                    weightedModifier = Modifier
                        .weight(0.15f)
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                        )
                )
            }

            val error = state.error
            LaunchedEffect(error?.value) {
                coroutineScope.launch {
                    error?.value?.let { errorText ->
                        hostState
                            .showSnackbar(errorText)
                    }
                    viewModel.onEvent(RecordsScreenEvent.OnErrorEventShown)
                }
            }
        }

        if (state.isLoading) {
            Loading()
        }
    }
}

@Composable
private fun TeamTitle(
    teamName: String
) {
    Text(
        text = teamName.uppercase(),
        fontSize = 30.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W700,
        letterSpacing = 0.1.em,
        textAlign = TextAlign.Center,
        color = Color.White,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(top = 45.dp)
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
            )
    )
}

@Composable
private fun Buttons(
    viewModel: RecordsViewModel,
    weightedModifier: Modifier = Modifier
) {
    Row(
        modifier = weightedModifier
    ) {
        val weightedModifier = Modifier.weight(1f)
        Button("Сохранить\nТочку", weightedModifier) {
            viewModel.onEvent(
                RecordsScreenEvent.TakeNewRecord
            )
        }
        Button("Сохранить\r\nФайл", weightedModifier) {
            viewModel.onEvent(
                RecordsScreenEvent.SaveCSVFileWithRecords
            )
        }
    }
}

@Composable
private fun Record(
    recordVO: RecordVO,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .padding(vertical = 15.dp)
            .padding(horizontal = 15.dp)
    ) {
        RecordTextLine("время - ${recordVO.time}")
        RecordTextLine("lat - ${recordVO.latitude}")
        RecordTextLine("long - ${recordVO.longitude}")
        RecordTextLine("alt = ${recordVO.altitude}")
    }
}

@Composable
private fun RecordTextLine(
    text: String
) {
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
            .fillMaxWidth()
            .wrapContentHeight()
            .drawBehind {
                drawRoundRect(color = Color.Black, style = stroke)
            }
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.W700,
            letterSpacing = 0.1.em,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(15.dp)
        )
    }
}

@Composable
private fun Button(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val stroke = Stroke(
        width = 10f,
        pathEffect = PathEffect.cornerPathEffect(50f)
    )

    Row(
        horizontalArrangement = Arrangement.Absolute.Center,
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(vertical = 15.dp)
            .wrapContentHeight()
            .drawBehind {
                drawRoundRect(color = Color.White, style = stroke)
            }
            .clickable { onClick() }
    ) {
        Text(
            text = text.uppercase(),
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.W700,
            letterSpacing = 0.1.em,
            color = Color.White,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(30.dp)
        )
    }
}