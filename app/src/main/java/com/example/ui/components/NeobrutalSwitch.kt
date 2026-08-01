package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NeobrutalSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color = Color(0xFF1B4F72), // Elegant Deep Blue matching mockup
    inactiveColor: Color = Color(0xFFEAEDED), // Light Grey
    modifier: Modifier = Modifier
) {
    val width = 52.dp
    val height = 26.dp
    val thumbSize = 20.dp
    val maxOffset = 26.dp

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) maxOffset else 0.dp,
        label = "SwitchThumbOffset"
    )

    Box(
        modifier = modifier
            .size(width, height)
            .border(2.dp, Color.Black)
            .background(if (checked) activeColor else inactiveColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onCheckedChange(!checked)
            }
            .padding(2.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .border(1.5.dp, Color.Black)
                .background(if (checked) Color.Black else Color.White)
        )
    }
}
