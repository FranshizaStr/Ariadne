package com.franshizastr.designsystem.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedTitle(
    text: String
) {
    val scope = LocalLifecycleOwner.current.lifecycleScope
    var animationProgress by remember { mutableStateOf("") }
    val text by remember { mutableStateOf(text) }

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