package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeobrutalCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InferenceTuningCard(
    temperature: Float,
    onTemperatureChanged: (Float) -> Unit,
    maxTokens: Int,
    onMaxTokensChanged: (Int) -> Unit,
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
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Inference Tuning",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TEMPERATURE Slider Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "TEMPERATURE",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )

                    // Black box with white value text
                    Box(
                        modifier = Modifier
                            .background(Color.Black)
                            .border(1.5.dp, Color.Black)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = String.format("%.1f", temperature),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Neobrutal Slider with thick black track and yellow square thumb
                Slider(
                    value = temperature,
                    onValueChange = onTemperatureChanged,
                    valueRange = 0.1f..1.0f,
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.Black,
                        inactiveTrackColor = Color.Black,
                        thumbColor = Color(0xFFF4D03F)
                    ),
                    thumb = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(2.dp, Color.Black)
                                .background(Color(0xFFF4D03F))
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Lower is focused and deterministic, higher is creative and random.",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // MAX TOKENS Slider Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "MAX TOKENS",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )

                    // Black box with white value text
                    Box(
                        modifier = Modifier
                            .background(Color.Black)
                            .border(1.5.dp, Color.Black)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$maxTokens",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Neobrutal Slider for Max Tokens
                Slider(
                    value = maxTokens.toFloat(),
                    onValueChange = { onMaxTokensChanged(it.toInt()) },
                    valueRange = 512f..4096f,
                    steps = 6, // 512, 1024, 2048, 4096 etc
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.Black,
                        inactiveTrackColor = Color.Black,
                        thumbColor = Color(0xFFF4D03F)
                    ),
                    thumb = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(2.dp, Color.Black)
                                .background(Color(0xFFF4D03F))
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Controls the maximum length of generated responses.",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
