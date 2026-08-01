package com.example.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AiModelInfo
import com.example.ui.components.NeobrutalCard

@Composable
fun EmptyChatWelcomeView(
    activeModel: AiModelInfo,
    onSelectPrompt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // High contrast Neobrutal yellow sun badge
        NeobrutalCard(
            modifier = Modifier.size(90.dp),
            backgroundColor = Color(0xFFF4D03F), // Yellow
            borderColor = Color.Black,
            borderWidth = 2.dp,
            shadowOffset = 5.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = "Ray AI Sun logo",
                    tint = Color.Black,
                    modifier = Modifier.size(46.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Title
        Text(
            text = "RAY AI",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Subtitle
        Text(
            text = "YOUR OFFLINE INTELLIGENCE ENGINE",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Info card in Neobrutalist styling
        NeobrutalCard(
            modifier = Modifier.fillMaxWidth(0.9f),
            backgroundColor = Color.White,
            borderColor = Color.Black,
            borderWidth = 2.dp,
            shadowOffset = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "STATUS: ACTIVE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black,
                    modifier = Modifier
                        .background(Color(0xFF85C1E9)) // Light Blue
                        .border(1.5.dp, Color.Black)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Powered by ${activeModel.name}\n100% on-device local execution.\nNo data ever leaves this device.",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
