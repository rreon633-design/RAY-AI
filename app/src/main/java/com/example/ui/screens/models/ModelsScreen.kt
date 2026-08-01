package com.example.ui.screens.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AiModelInfo
import com.example.ui.theme.ImmersiveCanvas
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.ModelsUiState

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelsScreen(
    state: ModelsUiState,
    onSelectCategory: (com.example.domain.model.ModelCategory?) -> Unit,
    onStartDownload: (AiModelInfo) -> Unit,
    onDeleteModel: (String) -> Unit,
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
                            text = "Model Download Hub",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Small INT4 GGUF models optimized for mobile CPU",
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
            // Hardware Detection Banner
            state.deviceHardwareInfo?.let { hw ->
                val recModel = state.catalog.find { it.id == hw.recommendedModelId }
                val recEntity = recModel?.let { state.downloadedModelsMap[it.id] }
                val isDownloaded = recEntity?.isDownloaded == true
                val isDownloading = state.downloadingProgressMap.containsKey(recModel?.id)

                item {
                    DeviceRecommendationBanner(
                        hardwareInfo = hw,
                        recommendedModel = recModel,
                        isAlreadyDownloaded = isDownloaded,
                        isDownloading = isDownloading,
                        onDownloadClick = onStartDownload
                    )
                }
            }

            item {
                StorageUsageBanner(
                    usedBytes = state.totalStorageUsedBytes,
                    totalBytes = state.freeStorageBytes
                )
            }

            item {
                ModelFilterChips(
                    selectedCategory = state.selectedCategory,
                    onSelectCategory = onSelectCategory
                )
            }

            items(filteredCatalog, key = { it.id }) { model ->
                val entity = state.downloadedModelsMap[model.id]
                val progress = state.downloadingProgressMap[model.id]

                ModelCardItem(
                    model = model,
                    modelEntity = entity,
                    downloadProgress = progress,
                    onStartDownload = onStartDownload,
                    onDeleteModel = onDeleteModel
                )
            }
        }
    }
}

