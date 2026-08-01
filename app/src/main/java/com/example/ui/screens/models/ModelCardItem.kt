package com.example.ui.screens.models

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ModelEntity
import com.example.domain.engine.DownloadProgress
import com.example.domain.model.AiModelInfo
import com.example.ui.components.MetricBadge
import com.example.ui.components.StatusChip
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.OnAccentPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ModelCardItem(
    model: AiModelInfo,
    modelEntity: ModelEntity?,
    downloadProgress: DownloadProgress?,
    onStartDownload: (AiModelInfo) -> Unit,
    onDeleteModel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDownloaded = modelEntity?.isDownloaded == true
    val isDownloading = downloadProgress != null || modelEntity?.isDownloading == true

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (model.isRecommended) AccentPurple else ImmersiveBorder,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = model.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (model.isRecommended) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Recommended",
                            tint = AccentPurple,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                if (isDownloaded) {
                    StatusChip(
                        text = "Downloaded",
                        backgroundColor = Color(0x334ADE80),
                        textColor = SuccessGreen
                    )
                } else {
                    StatusChip(
                        text = model.sizeFormatted,
                        backgroundColor = ImmersiveSurface,
                        textColor = TextSecondary
                    )
                }
            }

            Text(
                text = "By ${model.developer} • ${model.parameterCount} Params • ${model.quantization}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted,
                modifier = Modifier.padding(top = 2.dp)
            )

            Text(
                text = model.description,
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier.padding(vertical = 8.dp),
                lineHeight = 16.sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                MetricBadge(
                    icon = Icons.Default.Speed,
                    label = "~${model.targetTokensPerSec} t/s",
                    containerColor = ImmersiveSurface,
                    contentColor = AccentPurple
                )
                MetricBadge(
                    icon = Icons.Default.Memory,
                    label = "${model.ramRequiredMb} MB RAM",
                    containerColor = ImmersiveSurface,
                    contentColor = TextSecondary
                )
            }

            if (isDownloading) {
                val progress = downloadProgress?.progressPercent ?: modelEntity?.downloadProgressPercent ?: 0f
                val speed = downloadProgress?.speedMbPerSec ?: 0.0
                val eta = downloadProgress?.etaSeconds ?: 0

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Downloading INT4 Model: ${progress.toInt()}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentPurple
                        )
                        Text(
                            text = "$speed MB/s • ${eta}s left",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = AccentPurple,
                        trackColor = ImmersiveSurface
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isDownloaded) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Ready",
                                tint = SuccessGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Ready for Offline CPU Chat",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SuccessGreen
                            )
                        }

                        IconButton(onClick = { onDeleteModel(model.id) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete model",
                                tint = ErrorRed
                            )
                        }
                    } else {
                        Button(
                            onClick = { onStartDownload(model) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentPurple,
                                contentColor = OnAccentPurple
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Download Model (${model.sizeFormatted})", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

