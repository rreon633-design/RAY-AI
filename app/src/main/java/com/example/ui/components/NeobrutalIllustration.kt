package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NeobrutalIllustration(modifier: Modifier = Modifier) {
    // Outer Container: Gray background with thick black border and hard shadow
    NeobrutalCard(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        backgroundColor = Color(0xFFE5E5E5), // Cool grey background
        borderColor = Color.Black,
        borderWidth = 2.dp,
        shadowOffset = 5.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Draw diagonal grid lines or subtle crossing lines for architectural look
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 1.dp.toPx()
                // Diagonal X lines
                drawLine(
                    color = Color.Black.copy(alpha = 0.12f),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = Color.Black.copy(alpha = 0.12f),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Phone 1: AI Insights
                NeobrutalCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    backgroundColor = Color.White,
                    borderColor = Color.Black,
                    borderWidth = 2.dp,
                    shadowOffset = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        // Header
                        Text(
                            text = "AI Insights",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.Black)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Draw miniature chart in Compose Canvas
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            // Mini line chart
                            val pathPoints = listOf(
                                Offset(0f, size.height * 0.8f),
                                Offset(size.width * 0.25f, size.height * 0.6f),
                                Offset(size.width * 0.5f, size.height * 0.3f),
                                Offset(size.width * 0.75f, size.height * 0.45f),
                                Offset(size.width, size.height * 0.1f)
                            )
                            for (i in 0 until pathPoints.size - 1) {
                                drawLine(
                                    color = Color.Black,
                                    start = pathPoints[i],
                                    end = pathPoints[i + 1],
                                    strokeWidth = 2.dp.toPx()
                                )
                            }
                            // Draw yellow highlight circle at peak
                            drawCircle(
                                color = Color(0xFFF4D03F),
                                radius = 4.dp.toPx(),
                                center = pathPoints.last(),
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                        }
                    }
                }

                // Phone 2: Ray AI Chat UI
                NeobrutalCard(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight(),
                    backgroundColor = Color.White,
                    borderColor = Color.Black,
                    borderWidth = 2.dp,
                    shadowOffset = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Header bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ray AI Chat",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Black
                            )
                            // Mini Status badge
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Color(0xFFF4D03F))
                                    .border(1.dp, Color.Black)
                            )
                        }

                        // Mini Message Box representation
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .background(Color(0xFF6BB6EC))
                                .border(1.5.dp, Color.Black)
                                .padding(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(4.dp)
                                    .background(Color.Black)
                            )
                        }

                        // Mini Text Input box representation at the bottom
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(18.dp)
                                    .background(Color.White)
                                    .border(1.5.dp, Color.Black)
                            )
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(18.dp)
                                    .background(Color(0xFFF4D03F))
                                    .border(1.5.dp, Color.Black)
                            )
                        }
                    }
                }
            }
        }
    }
}
