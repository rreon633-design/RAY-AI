package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import com.example.ui.components.NeobrutalCard
import com.example.ui.components.NeobrutalSwitch

@Composable
fun AppSettingsCard(
    isDarkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    isStreamingResponse: Boolean,
    onStreamingResponseChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    NeobrutalCard(
        backgroundColor = Color.White,
        shadowOffset = 6.dp,
        borderWidth = 2.5.dp,
        cornerRadius = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Section Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "App Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dark Mode switch card box
            NeobrutalCard(
                backgroundColor = Color.White,
                shadowOffset = 0.dp,
                borderWidth = 2.dp,
                cornerRadius = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Dark Mode",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Switch to high-contrast night vision",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    NeobrutalSwitch(
                        checked = isDarkMode,
                        onCheckedChange = onDarkModeChanged,
                        activeColor = Color.Black, // Black active mode
                        inactiveColor = Color(0xFFEAEDED)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Streaming Response switch card box
            NeobrutalCard(
                backgroundColor = Color.White,
                shadowOffset = 0.dp,
                borderWidth = 2.dp,
                cornerRadius = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Streaming Response",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Watch words appear in real-time",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    NeobrutalSwitch(
                        checked = isStreamingResponse,
                        onCheckedChange = onStreamingResponseChanged,
                        activeColor = Color(0xFF1B4F72), // Blue active mode matching screenshot
                        inactiveColor = Color(0xFFEAEDED)
                    )
                }
            }
        }
    }
}
