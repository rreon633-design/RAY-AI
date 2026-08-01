package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NeobrutalProgressBar(
    progress: Float, // 0.0 to 1.0
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFEEEEEE),
    progressColor: Color = Color(0xFFF4D03F), // Yellow
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    barHeight: Dp = 16.dp
) {
    val clampedProgress = progress.coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight)
            .background(backgroundColor)
            .border(width = borderWidth, color = borderColor)
    ) {
        if (clampedProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(clampedProgress)
                    .fillMaxHeight()
                    .background(progressColor)
                    .then(
                        if (clampedProgress < 1f) {
                            Modifier.border(width = borderWidth, color = borderColor)
                        } else Modifier
                    )
            )
        }
    }
}
