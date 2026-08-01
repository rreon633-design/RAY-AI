package com.example.data.repository

import com.example.data.local.ModelDao
import com.example.data.local.ModelEntity
import com.example.domain.model.AiModelInfo
import kotlinx.coroutines.flow.Flow

class ModelRepository(private val modelDao: ModelDao) {
    val downloadedModels: Flow<List<ModelEntity>> = modelDao.getAllModels()

    suspend fun getModel(id: String): ModelEntity? = modelDao.getModelById(id)

    suspend fun saveModel(model: ModelEntity) {
        modelDao.insertOrUpdateModel(model)
    }

    suspend fun setActiveModel(activeModelId: String) {
        modelDao.setActiveModel(activeModelId)
    }

    suspend fun deleteModel(modelId: String) {
        modelDao.deleteModel(modelId)
    }

    suspend fun initializeDefaultModels() {
        // Pre-populate Qwen 0.5B and Gemma 2B as ready-to-run or pre-cached demo models
        val existingQwen = modelDao.getModelById("qwen-2.5-0.5b-int4")
        if (existingQwen == null) {
            modelDao.insertOrUpdateModel(
                ModelEntity(
                    id = "qwen-2.5-0.5b-int4",
                    name = "Qwen 2.5 0.5B",
                    isDownloaded = true,
                    downloadedBytes = 368_000_000L,
                    totalBytes = 368_000_000L,
                    downloadProgressPercent = 100f,
                    isActiveModel = true
                )
            )
        }

        val existingGemma = modelDao.getModelById("gemma-2-2b-int4")
        if (existingGemma == null) {
            modelDao.insertOrUpdateModel(
                ModelEntity(
                    id = "gemma-2-2b-int4",
                    name = "Google Gemma 2 2B",
                    isDownloaded = true,
                    downloadedBytes = 1_380_000_000L,
                    totalBytes = 1_380_000_000L,
                    downloadProgressPercent = 100f,
                    isActiveModel = false
                )
            )
        }
    }
}
