package com.example.ui.screens.models

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
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
import com.example.ui.components.NeobrutalProgressBar
import com.example.domain.engine.DownloadProgress
import com.example.domain.model.AiModelInfo

@Composable
fun GlobalDownloadProgressCard(
    downloadingModels: Map<String, DownloadProgress>,
    catalog: List<AiModelInfo>,
    modifier: Modifier = Modifier
) {
    if (downloadingModels.isEmpty()) return

    // Get the first active download in progress
    val (modelId, progress) = downloadingModels.entries.first()
    val modelInfo = catalog.find { it.id == modelId }
    val displayName = (modelInfo?.name ?: "LOCAL MODEL").uppercase()

    val percent = progress.progressPercent.toInt()
    val speed = String.format("%.1f", progress.speedMbPerSec)

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
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "DOWNLOADING: $percent% - $speed MB/s",
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = displayName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                modifier = Modifier.padding(start = 28.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Light Blue Neobrutalist progress bar
            NeobrutalProgressBar(
                progress = progress.progressPercent / 100f,
                progressColor = Color(0xFF5DADE2), // light blue
                backgroundColor = Color(0xFFF2F4F4),
                barHeight = 12.dp,
                borderWidth = 2.dp
            )
        }
    }
}
