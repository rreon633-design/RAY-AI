package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveCanvas
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.OnAccentPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    memoriesList: List<String>,
    onAddMemory: (String) -> Unit,
    onDeleteMemory: (Int) -> Unit,
    onClearAllMemories: () -> Unit,
    onSelectThemeMode: (String) -> Unit,
    onDownloadAlertsChanged: (Boolean) -> Unit,
    onDailyBriefChanged: (Boolean) -> Unit,
    onUsageWarningsChanged: (Boolean) -> Unit,
    onSubmitBugReport: (category: String, description: String) -> Unit,
    onClearBugReportStatus: () -> Unit,
    onUpdateCpuThreads: (Int) -> Unit,
    onUpdateContextWindow: (Int) -> Unit,
    onUpdateTemperature: (Float) -> Unit,
    onUpdateSystemPrompt: (String) -> Unit,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary
                            )
                        }
                    }
                },
                title = {
                    Column {
                        Text(
                            text = "Settings & Preferences",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Memories, Appearance, Engine Tuning, and Diagnostics",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ImmersiveSurface)
            )
        },
        containerColor = ImmersiveCanvas,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(ImmersiveCanvas)
        ) {
            // 1. Appearance & Theme Section
            item {
                AppearanceSection(
                    currentThemeMode = state.settings.themeMode,
                    onSelectThemeMode = onSelectThemeMode
                )
            }

            // 2. AI Memories Section
            item {
                MemoriesSection(
                    memories = memoriesList,
                    onAddMemory = onAddMemory,
                    onDeleteMemory = onDeleteMemory,
                    onClearAllMemories = onClearAllMemories
                )
            }

            // 3. CPU Threads Engine Settings
            item {
                ThreadSelector(
                    selectedThreads = state.settings.cpuThreads,
                    onThreadSelected = onUpdateCpuThreads
                )
            }

            // 4. Context Window & Temperature Settings
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ImmersiveBorder, shape = RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Context Window Size",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Maximum token history loaded in CPU RAM per chat session",
                            fontSize = 12.sp,
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf(512, 1024, 2048, 4096).forEach { size ->
                                FilterChip(
                                    selected = state.settings.contextWindow == size,
                                    onClick = { onUpdateContextWindow(size) },
                                    label = { Text("$size tok") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AccentPurple,
                                        selectedLabelColor = OnAccentPurple,
                                        containerColor = ImmersiveSurface,
                                        labelColor = TextSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ImmersiveBorder, shape = RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Temperature (Creativity)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${Math.round(state.settings.temperature * 10) / 10.0}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentPurple
                            )
                        }

                        Slider(
                            value = state.settings.temperature,
                            onValueChange = onUpdateTemperature,
                            valueRange = 0.1f..1.2f,
                            steps = 10,
                            colors = SliderDefaults.colors(
                                thumbColor = AccentPurple,
                                activeTrackColor = AccentPurple,
                                inactiveTrackColor = ImmersiveSurface
                            )
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("0.1 (Precise/Code)", fontSize = 10.sp, color = TextMuted)
                            Text("1.2 (Creative)", fontSize = 10.sp, color = TextMuted)
                        }
                    }
                }
            }

            // 5. System Prompt Editor
            item {
                SystemPromptEditor(
                    systemPrompt = state.settings.systemPrompt,
                    onPromptChanged = onUpdateSystemPrompt
                )
            }

            // 6. Notifications & Alerts
            item {
                NotificationsSection(
                    downloadAlerts = state.settings.downloadAlertsEnabled,
                    onDownloadAlertsChanged = onDownloadAlertsChanged,
                    dailyBrief = state.settings.dailyBriefEnabled,
                    onDailyBriefChanged = onDailyBriefChanged,
                    usageWarnings = state.settings.usageWarningsEnabled,
                    onUsageWarningsChanged = onUsageWarningsChanged
                )
            }

            // 7. Report Bug Section
            item {
                ReportBugSection(
                    statusMessage = state.bugReportStatus,
                    onSubmitReport = onSubmitBugReport,
                    onClearStatus = onClearBugReportStatus
                )
            }

            // 8. About Section
            item {
                AboutSection()
            }
        }
    }
}


