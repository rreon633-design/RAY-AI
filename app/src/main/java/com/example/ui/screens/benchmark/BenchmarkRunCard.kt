package com.example.ui.screens.benchmark

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MetricBadge
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.OnAccentPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, ImmersiveBorder, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Run INT4 CPU Matrix Math Benchmark",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Measures peak tokens/sec inference speed before downloading models",
                fontSize = 12.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Select CPU Threads for Test:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 6.dp, bottom = 14.dp)
            ) {
                listOf(2, 4, 6, 8).forEach { threads ->
                    FilterChip(
                        selected = selectedThreads == threads,
                        onClick = { selectedThreads = threads },
                        label = { Text("$threads Cores") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentPurple,
                            selectedLabelColor = OnAccentPurple,
                            containerColor = ImmersiveSurface,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            if (isTesting) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stageText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentPurple
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progressPercent / 100f },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = AccentPurple,
                        trackColor = ImmersiveSurface
                    )
                }
            } else {
                Button(
                    onClick = { onRunTest(selectedThreads) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPurple,
                        contentColor = OnAccentPurple
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Start CPU Speed Test ($selectedThreads Threads)", fontWeight = FontWeight.Bold)
                }
            }

            if (lastResult != null && !isTesting) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = ImmersiveBorderSubtle)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "BENCHMARK RESULTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MetricBadge(
                        icon = Icons.Default.Speed,
                        label = "⚡ ${lastResult.peakTokensPerSec} Peak t/s",
                        containerColor = Color(0x334ADE80),
                        contentColor = SuccessGreen
                    )
                    MetricBadge(
                        icon = Icons.Default.Thermostat,
                        label = lastResult.thermalState,
                        containerColor = Color(0x33F59E0B),
                        contentColor = Color(0xFFFFB74D)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "• Matrix Math: ${lastResult.gflopsPerformance} GFLOPS\n" +
                           "• TTFT Latency: ${lastResult.averageLatencyMs} ms\n" +
                           "• Efficiency Score: ${lastResult.threadEfficiencyScore}%\n" +
                           "• Recommended Model: ${lastResult.recommendedModel}",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

