package com.example.data.repository

import com.example.data.local.ModelEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ModelRepositoryTest {

    private lateinit var fakeDao: FakeModelDao
    private lateinit var modelRepository: ModelRepository

    @Before
    fun setup() {
        fakeDao = FakeModelDao()
        modelRepository = ModelRepository(fakeDao)
    }

    @Test
    fun `getModel returns model if exists`() = runTest {
        val expectedModel = ModelEntity(id = "test-model", name = "Test", isDownloaded = true, downloadedBytes = 0L, totalBytes = 0L, downloadProgressPercent = 100f, isActiveModel = false)
        fakeDao.models.add(expectedModel)
        
        val result = modelRepository.getModel("test-model")
        assertEquals(expectedModel, result)
    }

    @Test
    fun `saveModel calls insertOrUpdateModel`() = runTest {
        val model = ModelEntity(id = "test-model", name = "Test", isDownloaded = true, downloadedBytes = 0L, totalBytes = 0L, downloadProgressPercent = 100f, isActiveModel = false)
        modelRepository.saveModel(model)
        assertEquals(1, fakeDao.models.size)
    }

    @Test
    fun `initializeDefaultModels inserts models if they do not exist`() = runTest {
        modelRepository.initializeDefaultModels()
        assertEquals(2, fakeDao.models.size)
    }

    @Test
    fun `initializeDefaultModels skips inserts if models already exist`() = runTest {
        val model1 = ModelEntity(id = "qwen-2.5-0.5b-int4", name = "Qwen", isDownloaded = true, downloadedBytes = 0L, totalBytes = 0L, downloadProgressPercent = 100f, isActiveModel = false)
        val model2 = ModelEntity(id = "gemma-2-2b-int4", name = "Gemma", isDownloaded = true, downloadedBytes = 0L, totalBytes = 0L, downloadProgressPercent = 100f, isActiveModel = false)
        fakeDao.models.add(model1)
        fakeDao.models.add(model2)
        
        modelRepository.initializeDefaultModels()
        assertEquals(2, fakeDao.models.size)
    }
}
