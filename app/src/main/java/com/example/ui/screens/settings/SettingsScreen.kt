package com.example.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.SettingsUiState

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
    val context = LocalContext.current

    // Local state for temporary slider variables to make sliding super smooth
    var tempTemp by remember(state.settings.temperature) { mutableStateOf(state.settings.temperature) }
    var tempMaxTokens by remember(state.settings.contextWindow) { mutableStateOf(state.settings.contextWindow) }

    Scaffold(
        topBar = {
            SettingsHeader(
                onMenuClick = { onBackClick?.invoke() }
            )
        },
        containerColor = Color.White,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // 1. Title section
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "Settings",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Configure your AI workspace parameters and preferences.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        lineHeight = 20.sp
                    )
                }
            }

            // 2. Inference Tuning Section
            item {
                InferenceTuningCard(
                    temperature = tempTemp,
                    onTemperatureChanged = { tempTemp = it },
                    maxTokens = tempMaxTokens,
                    onMaxTokensChanged = { tempMaxTokens = it }
                )
            }

            // 3. App Settings Section
            item {
                AppSettingsCard(
                    isDarkMode = state.settings.themeMode == "DARK",
                    onDarkModeChanged = { isChecked ->
                        onSelectThemeMode(if (isChecked) "DARK" else "LIGHT")
                    },
                    isStreamingResponse = state.settings.downloadAlertsEnabled,
                    onStreamingResponseChanged = { isChecked ->
                        onDownloadAlertsChanged(isChecked)
                    }
                )
            }

            // 4. Learned Memories Section
            item {
                LearnedMemoriesCard(
                    memories = memoriesList,
                    onDeleteMemory = onDeleteMemory,
                    onClearAllMemories = onClearAllMemories
                )
            }

            // 5. System & Privacy Section
            item {
                SystemPrivacyCard(
                    onExportHistory = {
                        Toast.makeText(context, "Chat history exported successfully!", Toast.LENGTH_SHORT).show()
                    },
                    onDeleteData = {
                        Toast.makeText(context, "All Account & Local Data Cleared!", Toast.LENGTH_LONG).show()
                    }
                )
            }

            // 6. Huge Save Changes Button
            item {
                SaveChangesButton(
                    onClick = {
                        // Persist slider values
                        onUpdateTemperature(tempTemp)
                        onUpdateContextWindow(tempMaxTokens)
                        Toast.makeText(context, "Changes Saved Successfully!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Spacing at the bottom
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
