package com.example.data.repository

import com.example.data.local.ModelDao
import com.example.data.local.ModelEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeModelDao : ModelDao {
    val models = mutableListOf<ModelEntity>()

    override fun getAllModels(): Flow<List<ModelEntity>> = flowOf(models)

    override suspend fun getModelById(id: String): ModelEntity? = models.find { it.id == id }

    override suspend fun insertOrUpdateModel(model: ModelEntity) {
        val index = models.indexOfFirst { it.id == model.id }
        if (index != -1) {
            models[index] = model
        } else {
            models.add(model)
        }
    }

    override suspend fun setActiveModel(activeModelId: String) {
        for (i in models.indices) {
            models[i] = models[i].copy(isActiveModel = models[i].id == activeModelId)
        }
    }

    override suspend fun deleteModel(modelId: String) {
        models.removeIf { it.id == modelId }
    }
}
