package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NeobrutalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF4D03F), // Classical Neobrutal yellow
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    shadowOffset: Dp = 4.dp,
    shadowColor: Color = Color.Black,
    cornerRadius: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    val currentOffset = if (isPressed) shadowOffset else 0.dp
    val actualShadowOffset = if (isPressed) 0.dp else shadowOffset

    Box(
        modifier = modifier
            .offset(x = currentOffset, y = currentOffset)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        NeobrutalCard(
            backgroundColor = backgroundColor,
            borderColor = borderColor,
            borderWidth = borderWidth,
            shadowOffset = actualShadowOffset,
            shadowColor = shadowColor,
            cornerRadius = cornerRadius,
            content = content
        )
    }
}
