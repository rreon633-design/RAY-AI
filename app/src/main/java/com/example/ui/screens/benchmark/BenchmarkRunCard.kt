package com.example.ui.screens.benchmark

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeobrutalButton
import com.example.ui.components.NeobrutalCard
import com.example.ui.components.NeobrutalProgressBar
import com.example.ui.viewmodel.BenchmarkResult

@Composable
fun BenchmarkRunCard(
    isTesting: Boolean,
    progressPercent: Float,
    stageText: String,
    lastResult: BenchmarkResult?,
    onRunTest: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedThreads by remember { mutableStateOf(4) }

    NeobrutalCard(
        backgroundColor = Color.White, // Always make background white
        borderColor = Color.Black,
        shadowColor = Color.Black,
        borderWidth = 2.dp,
        shadowOffset = 5.dp,
        cornerRadius = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "CPU INFERENCE BENCHMARK",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color.Black
            )
            Text(
                text = "MEASURES PEAK TOKENS/SEC SPEED BEFORE DOWNLOADING",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SELECT THREADS:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color.Black
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
            ) {
                listOf(2, 4, 6, 8).forEach { threads ->
                    val isSelected = selectedThreads == threads
                    Box(
                        modifier = Modifier.clickable { selectedThreads = threads }
                    ) {
                        NeobrutalCard(
                            backgroundColor = if (isSelected) Color(0xFFFCDF46) else Color.White,
                            borderColor = Color.Black,
                            shadowColor = Color.Black,
                            borderWidth = 1.5.dp,
                            shadowOffset = if (isSelected) 2.dp else 1.dp,
                            cornerRadius = 0.dp
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$threads CORES",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            if (isTesting) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stageText.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NeobrutalProgressBar(
                        progress = progressPercent / 100f,
                        barHeight = 16.dp,
                        progressColor = Color(0xFFFCDF46),
                        backgroundColor = Color.White,
                        borderColor = Color.Black,
                        borderWidth = 1.5.dp
                    )
                }
            } else {
                NeobrutalButton(
                    onClick = { onRunTest(selectedThreads) },
                    backgroundColor = Color(0xFFFCDF46), // Yellow accent
                    borderColor = Color.Black,
                    shadowOffset = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "START CPU SPEED TEST",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black
                        )
                    }
                }
            }

            if (lastResult != null && !isTesting) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.Black, modifier = Modifier.height(2.dp))
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "BENCHMARK RESULTS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Speed badge
                    Box(modifier = Modifier.weight(1f)) {
                        NeobrutalCard(
                            backgroundColor = Color(0xFFA7F3D0), // Soft green
                            borderColor = Color.Black,
                            shadowColor = Color.Black,
                            borderWidth = 1.5.dp,
                            shadowOffset = 2.dp,
                            cornerRadius = 0.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Speed, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                                Text("${lastResult.peakTokensPerSec} PEAK T/S", fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, color = Color.Black)
                            }
                        }
                    }

                    // Temp badge
                    Box(modifier = Modifier.weight(1f)) {
                        NeobrutalCard(
                            backgroundColor = Color(0xFFFEF08A), // Soft yellow
                            borderColor = Color.Black,
                            shadowColor = Color.Black,
                            borderWidth = 1.5.dp,
                            shadowOffset = 2.dp,
                            cornerRadius = 0.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Thermostat, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                                Text(lastResult.thermalState.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, color = Color.Black)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Numerical facts inside a monospace neobrutalist listing
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .border(width = 1.5.dp, color = Color.Black)
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("• MATH THROUGHPUT: ${lastResult.gflopsPerformance} GFLOPS", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color.Black)
                        Text("• FIRST TOKEN LATENCY: ${lastResult.averageLatencyMs} MS", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color.Black)
                        Text("• THREAD EFFICIENCY: ${lastResult.threadEfficiencyScore}%", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color.Black)
                        Text("• MATCHED MODEL: ${lastResult.recommendedModel.uppercase()}", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color.Black)
                    }
                }
            }
        }
    }
}
