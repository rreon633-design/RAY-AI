package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NeobrutalIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    shadowOffset: Dp = 3.dp,
    shadowColor: Color = Color.Black,
    iconColor: Color = Color.Black,
    iconSize: Dp = 20.dp,
    cornerRadius: Dp = 0.dp
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
            cornerRadius = cornerRadius
        ) {
            Box(
                modifier = Modifier.padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = iconColor,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}
