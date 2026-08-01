package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NeobrutalStatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF4D03F), // Neobrutalist yellow
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    textColor: Color = Color.Black
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .border(width = borderWidth, color = borderColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.5.sp
        )
    }
}
