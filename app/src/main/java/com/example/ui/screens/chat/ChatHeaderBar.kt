package com.example.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import com.example.domain.model.AiModelInfo
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.ModelStatus

private data class StatusStyle(
    val bgColor: Color,
    val textColor: Color,
    val label: String
)

@Composable
fun ModelStatusChip(
    status: ModelStatus,
    modifier: Modifier = Modifier
) {
    val style = when (status) {
        ModelStatus.PROCESSING -> StatusStyle(
            bgColor = Color(0xFFF3E8FF),
            textColor = AccentPurple,
            label = "Processing"
        )
        ModelStatus.DOWNLOADING -> StatusStyle(
            bgColor = Color(0xFFEFF6FF),
            textColor = Color(0xFF2563EB),
            label = "Downloading"
        )
        ModelStatus.READY -> StatusStyle(
            bgColor = Color(0xFFDCFCE7),
            textColor = SuccessGreen,
            label = "Ready"
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(style.bgColor, shape = RoundedCornerShape(12.dp))
            .border(1.dp, style.textColor.copy(alpha = 0.35f), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(style.textColor, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = style.label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = style.textColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHeaderBar(
    activeModel: AiModelInfo,
    status: ModelStatus = ModelStatus.READY,
    onSelectModel: (AiModelInfo) -> Unit,
    onOpenDrawer: () -> Unit,
    onNewChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = ImmersiveSurface),
        navigationIcon = {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = TextPrimary
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(ImmersiveSurfaceVariant, shape = RoundedCornerShape(16.dp))
                            .clickable { dropdownExpanded = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Memory,
                            contentDescription = null,
                            tint = AccentPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = activeModel.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentPurple
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Switch Model",
                            tint = AccentPurple,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.background(ImmersiveSurfaceVariant)
                    ) {
                        Text(
                            text = "SELECT ACTIVE OFFLINE MODEL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                        AiModelInfo.CATALOG.forEach { model ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            text = model.name,
                                            fontWeight = if (model.id == activeModel.id) FontWeight.Bold else FontWeight.Normal,
                                            color = if (model.id == activeModel.id) AccentPurple else TextPrimary
                                        )
                                        Text(
                                            text = "${model.quantization} • ${model.sizeFormatted}",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                },
                                onClick = {
                                    onSelectModel(model)
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                ModelStatusChip(status = status)
            }
        },
        actions = {
            IconButton(onClick = onNewChat) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Chat",
                    tint = AccentPurple
                )
            }
        }
    )
}

