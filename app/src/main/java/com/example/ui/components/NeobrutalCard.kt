package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.neobrutalShadow(
    offset: Dp = 4.dp,
    color: Color = Color.Black,
    cornerRadius: Dp = 0.dp
) = this.drawBehind {
    val offsetPx = offset.toPx()
    if (cornerRadius > 0.dp) {
        val radiusPx = cornerRadius.toPx()
        drawRoundRect(
            color = color,
            topLeft = Offset(offsetPx, offsetPx),
            size = size,
            cornerRadius = CornerRadius(radiusPx, radiusPx)
        )
    } else {
        drawRect(
            color = color,
            topLeft = Offset(offsetPx, offsetPx),
            size = size
        )
    }
}

@Composable
fun NeobrutalCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    shadowOffset: Dp = 5.dp,
    shadowColor: Color = Color.Black,
    cornerRadius: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .neobrutalShadow(
                offset = shadowOffset,
                color = shadowColor,
                cornerRadius = cornerRadius
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        content()
    }
}
