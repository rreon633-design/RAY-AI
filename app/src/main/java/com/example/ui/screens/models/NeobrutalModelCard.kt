package com.example.ui.screens.models

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ModelEntity
import com.example.domain.engine.DownloadProgress
import com.example.domain.model.AiModelInfo
import com.example.ui.components.NeobrutalButton
import com.example.ui.components.NeobrutalCard
import com.example.ui.components.NeobrutalProgressBar

@Composable
fun NeobrutalModelCard(
    model: AiModelInfo,
    modelEntity: ModelEntity?,
    downloadProgress: DownloadProgress?,
    onStartDownload: (AiModelInfo) -> Unit,
    onDeleteModel: (String) -> Unit,
    onLoadModel: (String) -> Unit,
    onUnloadModel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDownloaded = modelEntity?.isDownloaded == true
    val isDownloading = downloadProgress != null || modelEntity?.isDownloading == true
    val isActive = modelEntity?.isActiveModel == true

    // 1. Determine Badge Text and Header Color
    val (badgeText, headerBgColor) = when {
        isActive -> "ACTIVE" to Color(0xFFEBF5FB) // Light sky blue
        isDownloaded -> "LOCAL" to Color(0xFFFDEDEC) // Soft peach / pink
        model.tags.any { it.lowercase().contains("vision") || it.lowercase().contains("multimodal") } -> "VISION" to Color(0xFFEAECEE) // Gray
        model.tags.any { it.lowercase().contains("embed") } -> "EMBEDDING" to Color(0xFFE8F8F5) // Soft teal
        model.parameterCount.lowercase().contains("billion") && model.parameterCount.substringBefore(" ").toDoubleOrNull()?.let { it >= 4.0 } == true -> "EXPERIMENTAL" to Color(0xFFFADBD8) // Soft reddish
        else -> "CLOUD" to Color(0xFFFCF3CF) // Soft warm yellow
    }

    NeobrutalCard(
        backgroundColor = Color.White,
        shadowOffset = 4.dp,
        borderWidth = 2.dp,
        cornerRadius = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            
            // --- HEADER BOX (Status & Name) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBgColor)
                    .padding(16.dp)
            ) {
                // Status badge & Size
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Badge with solid black border
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.5.dp, Color.Black)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }

                    // Size display
                    Text(
                        text = model.sizeFormatted,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Model title in bold, uppercase black font
                Text(
                    text = model.name.uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.Black,
                    letterSpacing = 0.5.sp
                )
            }

            // --- CRITICAL BLACK DIVIDER LINE ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )

            // --- BODY BOX (Description & CTA) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = model.description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                when {
                    isDownloading -> {
                        val progress = downloadProgress?.progressPercent ?: modelEntity?.downloadProgressPercent ?: 0f
                        val speed = String.format("%.1f", downloadProgress?.speedMbPerSec ?: 0.0)

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "DOWNLOADING: ${progress.toInt()}%",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                                Text(
                                    text = "$speed MB/s",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            NeobrutalProgressBar(
                                progress = progress / 100f,
                                progressColor = Color(0xFF5DADE2),
                                barHeight = 10.dp,
                                borderWidth = 1.5.dp
                            )
                        }
                    }
                    isActive -> {
                        // UNLOAD Button
                        NeobrutalButton(
                            onClick = { onUnloadModel(model.id) },
                            backgroundColor = Color(0xFFF4D03F), // Neobrutal yellow
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "UNLOAD",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                    isDownloaded -> {
                        // LOAD Button
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            NeobrutalButton(
                                onClick = { onLoadModel(model.id) },
                                backgroundColor = Color(0xFFF4D03F), // Neobrutal yellow
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "LOAD",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color.Black
                                    )
                                }
                            }
                            
                            // Simple trash/delete button
                            NeobrutalButton(
                                onClick = { onDeleteModel(model.id) },
                                backgroundColor = Color(0xFFFADBD8), // light red
                                modifier = Modifier.width(56.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "✖",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        // DOWNLOAD Button
                        NeobrutalButton(
                            onClick = { onStartDownload(model) },
                            backgroundColor = Color(0xFF5DADE2), // Light blue
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "DOWNLOAD",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
