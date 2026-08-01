package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NeobrutalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    backgroundColor: Color = Color.White,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    shadowOffset: Dp = 0.dp,
    shadowColor: Color = Color.Black,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = Int.MAX_VALUE
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (shadowOffset > 0.dp) {
                    Modifier.neobrutalShadow(offset = shadowOffset, color = shadowColor)
                } else Modifier
            )
            .background(backgroundColor)
            .border(width = borderWidth, color = borderColor)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            ),
            cursorBrush = SolidColor(Color.Black),
            keyboardOptions = keyboardOptions,
            maxLines = maxLines,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
