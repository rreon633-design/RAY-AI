package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NeobrutalStepCard(
    stepNumber: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    NeobrutalCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderColor = Color.Black,
        borderWidth = 2.dp,
        shadowOffset = 4.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stepNumber,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF0D7BB3)
                )
                
                Text(
                    text = title.uppercase(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = description,
                fontSize = 12.5.sp,
                color = Color.Black,
                lineHeight = 18.sp
            )
            
            if (content != null) {
                Spacer(modifier = Modifier.height(10.dp))
                content()
            }
        }
    }
}
