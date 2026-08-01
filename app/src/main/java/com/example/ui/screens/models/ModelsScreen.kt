package com.example.ui.screens.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.domain.model.AiModelInfo
import com.example.domain.model.ModelCategory
import com.example.ui.viewmodel.ModelsUiState

@Composable
fun ModelsScreen(
    state: ModelsUiState,
    onSelectCategory: (ModelCategory?) -> Unit,
    onStartDownload: (AiModelInfo) -> Unit,
    onDeleteModel: (String) -> Unit,
    onLoadModel: (String) -> Unit,
    onUnloadModel: (String) -> Unit,
    onDetectHardware: (android.content.Context) -> Unit = {},
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        onDetectHardware(context)
    }

    val filteredCatalog = state.catalog.filter { model ->
        state.selectedCategory == null || model.category == state.selectedCategory
    }

    Scaffold(
        topBar = {
            ModelsHubHeader(
                onMenuClick = { onBackClick?.invoke() }
            )
        },
        containerColor = Color.White,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // 1. Dynamic System RAM usage card matching mockup
            item {
                SystemRamUsageCard(
                    hardwareInfo = state.deviceHardwareInfo
                )
            }

            // 2. Dynamic CPU Load card with interactive ticking load
            item {
                CpuLoadCard(
                    hardwareInfo = state.deviceHardwareInfo
                )
            }

            // 3. Global download progress indicator matching mockup when downloads are running
            if (state.downloadingProgressMap.isNotEmpty()) {
                item {
                    GlobalDownloadProgressCard(
                        downloadingModels = state.downloadingProgressMap,
                        catalog = state.catalog
                    )
                }
            }

            // Spacer line separating system load from models catalog
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // 4. Custom Neobrutalist filter chips
            item {
                ModelFilterChips(
                    selectedCategory = state.selectedCategory,
                    onSelectCategory = onSelectCategory
                )
            }

            // 5. Neobrutalist Model Cards catalog
            items(filteredCatalog, key = { it.id }) { model ->
                val entity = state.downloadedModelsMap[model.id]
                val progress = state.downloadingProgressMap[model.id]

                NeobrutalModelCard(
                    model = model,
                    modelEntity = entity,
                    downloadProgress = progress,
                    onStartDownload = onStartDownload,
                    onDeleteModel = onDeleteModel,
                    onLoadModel = onLoadModel,
                    onUnloadModel = onUnloadModel
                )
            }
        }
    }
}
