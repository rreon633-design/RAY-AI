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

import com.example.ui.components.NeobrutalStatusBadge

@Composable
fun ModelStatusChip(
    status: ModelStatus,
    modifier: Modifier = Modifier
) {
    val text = when (status) {
        ModelStatus.PROCESSING -> "THINKING"
        ModelStatus.DOWNLOADING -> "FETCHING"
        ModelStatus.READY -> "ACTIVE"
    }
    val color = when (status) {
        ModelStatus.PROCESSING -> Color(0xFFE25C4E) // Coral red
        ModelStatus.DOWNLOADING -> Color(0xFF5DADE2) // Sky blue
        ModelStatus.READY -> Color(0xFFF4D03F) // Yellow
    }
    NeobrutalStatusBadge(
        text = text,
        backgroundColor = color,
        modifier = modifier
    )
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

    Column(modifier = modifier.fillMaxWidth().background(Color.White)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left menu hamburger icon
            IconButton(
                onClick = onOpenDrawer,
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, Color.Black)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black
                )
            }

            // Clickable Center Model Selector Area resembling the screenshot title
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { dropdownExpanded = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "RAY AI",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                        color = Color.Black,
                        letterSpacing = 1.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Model",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .border(2.dp, Color.Black)
                ) {
                    Text(
                        text = "SELECT ACTIVE OFFLINE MODEL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                    AiModelInfo.CATALOG.forEach { model ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = model.name,
                                        fontWeight = if (model.id == activeModel.id) FontWeight.Bold else FontWeight.Normal,
                                        color = if (model.id == activeModel.id) Color(0xFFF4D03F) else Color.Black,
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                    )
                                    Text(
                                        text = "${model.quantization} • ${model.sizeFormatted}",
                                        fontSize = 11.sp,
                                        color = Color.DarkGray,
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
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

            // Right active status badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModelStatusChip(status = status)

                // Plus New Chat icon styled nicely in Neobrutalism
                IconButton(
                    onClick = onNewChat,
                    modifier = Modifier
                        .size(40.dp)
                        .border(2.dp, Color.Black)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Chat",
                        tint = Color.Black
                    )
                }
            }
        }
        // Bold bottom separator line for Neobrutalist design
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.Black)
        )
    }
}

