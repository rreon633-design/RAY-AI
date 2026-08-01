package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ModelDao {
    @Query("SELECT * FROM downloaded_models")
    fun getAllModels(): Flow<List<ModelEntity>>

    @Query("SELECT * FROM downloaded_models WHERE id = :modelId LIMIT 1")
    suspend fun getModelById(modelId: String): ModelEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateModel(model: ModelEntity)

    @Query("UPDATE downloaded_models SET isActiveModel = CASE WHEN id = :activeModelId THEN 1 ELSE 0 END")
    suspend fun setActiveModel(activeModelId: String)

    @Query("DELETE FROM downloaded_models WHERE id = :modelId")
    suspend fun deleteModel(modelId: String)
}
