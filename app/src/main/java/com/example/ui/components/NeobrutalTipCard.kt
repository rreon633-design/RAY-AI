package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
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
fun NeobrutalTipCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    NeobrutalCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color(0xFF85C1E9), // Light sky blue
        borderColor = Color.Black,
        borderWidth = 2.dp,
        shadowOffset = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .border(1.5.dp, Color.Black)
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Pro Tip Icon",
                    tint = Color(0xFFF4D03F), // Yellow lightbulb tint
                    modifier = Modifier.size(16.dp)
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "PRO TIP: ${title.uppercase()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(3.dp))
                
                Text(
                    text = description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
