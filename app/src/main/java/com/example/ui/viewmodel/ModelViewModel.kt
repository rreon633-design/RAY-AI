package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ModelEntity
import com.example.data.repository.ModelRepository
import com.example.domain.engine.DownloadProgress
import com.example.domain.engine.ModelDownloader
import com.example.domain.model.AiModelInfo
import com.example.domain.model.ModelCategory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import android.content.Context
import com.example.util.DeviceHardwareDetector
import com.example.util.DeviceHardwareInfo

data class ModelsUiState(
    val catalog: List<AiModelInfo> = AiModelInfo.CATALOG,
    val downloadedModelsMap: Map<String, ModelEntity> = emptyMap(),
    val downloadingProgressMap: Map<String, DownloadProgress> = emptyMap(),
    val selectedCategory: ModelCategory? = null,
    val searchQuery: String = "",
    val totalStorageUsedBytes: Long = 0L,
    val freeStorageBytes: Long = 64_000_000_000L,
    val deviceHardwareInfo: DeviceHardwareInfo? = null
)

class ModelViewModel(
    private val modelRepository: ModelRepository
) : ViewModel() {

    private val downloader = ModelDownloader()
    private val activeDownloadJobs = mutableMapOf<String, Job>()

    private val _uiState = MutableStateFlow(ModelsUiState())
    val uiState: StateFlow<ModelsUiState> = _uiState.asStateFlow()

    init {
        observeDownloadedModels()
    }

    fun detectHardware(context: Context) {
        if (_uiState.value.deviceHardwareInfo == null) {
            val info = DeviceHardwareDetector.detect(context)
            _uiState.update { it.copy(deviceHardwareInfo = info) }
        }
    }

    private fun observeDownloadedModels() {
        viewModelScope.launch {
            modelRepository.downloadedModels.collect { entities ->
                val map = entities.associateBy { it.id }
                val storageUsed = entities.filter { it.isDownloaded }.sumOf { it.downloadedBytes }
                _uiState.update {
                    it.copy(
                        downloadedModelsMap = map,
                        totalStorageUsedBytes = storageUsed
                    )
                }
            }
        }
    }

    fun setCategoryFilter(category: ModelCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun startDownload(model: AiModelInfo) {
        if (activeDownloadJobs.containsKey(model.id)) return

        viewModelScope.launch {
            // Initial model entity save
            val initialEntity = ModelEntity(
                id = model.id,
                name = model.name,
                isDownloaded = false,
                isDownloading = true,
                downloadedBytes = 0L,
                totalBytes = model.sizeBytes,
                downloadProgressPercent = 0f
            )
            modelRepository.saveModel(initialEntity)

            val downloadJob = launch {
                downloader.downloadModelStream(model).collect { progress ->
                    _uiState.update { state ->
                        val newProgressMap = state.downloadingProgressMap.toMutableMap()
                        newProgressMap[model.id] = progress
                        state.copy(downloadingProgressMap = newProgressMap)
                    }

                    val updatedEntity = ModelEntity(
                        id = model.id,
                        name = model.name,
                        isDownloaded = progress.isCompleted,
                        isDownloading = !progress.isCompleted,
                        downloadedBytes = progress.downloadedBytes,
                        totalBytes = progress.totalBytes,
                        downloadProgressPercent = progress.progressPercent
                    )
                    modelRepository.saveModel(updatedEntity)
                }

                // Download Finished
                activeDownloadJobs.remove(model.id)
                _uiState.update { state ->
                    val newProgressMap = state.downloadingProgressMap.toMutableMap()
                    newProgressMap.remove(model.id)
                    state.copy(downloadingProgressMap = newProgressMap)
                }
            }

            activeDownloadJobs[model.id] = downloadJob
        }
    }

    fun cancelOrDeleteModel(modelId: String) {
        activeDownloadJobs[modelId]?.cancel()
        activeDownloadJobs.remove(modelId)

        viewModelScope.launch {
            modelRepository.deleteModel(modelId)
            _uiState.update { state ->
                val newProgressMap = state.downloadingProgressMap.toMutableMap()
                newProgressMap.remove(modelId)
                state.copy(downloadingProgressMap = newProgressMap)
            }
        }
    }

    fun loadModel(modelId: String) {
        viewModelScope.launch {
            modelRepository.setActiveModel(modelId)
        }
    }

    fun unloadModel(modelId: String) {
        viewModelScope.launch {
            modelRepository.setActiveModel("")
        }
    }
}
