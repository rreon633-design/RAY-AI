package com.example.ui.screens.models

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
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
import com.example.ui.components.NeobrutalCard
import com.example.ui.components.NeobrutalProgressBar
import com.example.util.DeviceHardwareInfo
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun CpuLoadCard(
    hardwareInfo: DeviceHardwareInfo?,
    modifier: Modifier = Modifier
) {
    // Generate a beautiful, life-like CPU fluctuation
    var cpuPercentage by remember { mutableStateOf(28) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(1500 + Random.nextLong(2000))
            val delta = Random.nextInt(-4, 5)
            cpuPercentage = (cpuPercentage + delta).coerceIn(12, 45)
        }
    }

    val coreCount = hardwareInfo?.cpuCores ?: 8
    val cpuName = if (coreCount >= 16) "THREADRIPPER" else "${coreCount}-CORES"

    NeobrutalCard(
        backgroundColor = Color.White,
        shadowOffset = 4.dp,
        cornerRadius = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "CPU LOAD ($cpuName)",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$cpuPercentage%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }

                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Yellow Neobrutalist progress bar matching the mockup
            NeobrutalProgressBar(
                progress = cpuPercentage / 100f,
                progressColor = Color(0xFFF4D03F), // Neobrutal yellow
                backgroundColor = Color(0xFFF2F4F4),
                barHeight = 12.dp,
                borderWidth = 2.dp
            )
        }
    }
}
