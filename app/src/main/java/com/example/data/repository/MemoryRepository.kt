package com.example.data.repository

import com.example.data.local.MemoryDao
import com.example.data.local.MemoryEntity
import kotlinx.coroutines.flow.Flow

class MemoryRepository(private val memoryDao: MemoryDao) {
    val allMemories: Flow<List<MemoryEntity>> = memoryDao.getAllMemories()

    suspend fun addMemory(key: String, value: String, category: String = "General"): Long {
        val memory = MemoryEntity(key = key, value = value, category = category)
        return memoryDao.insertMemory(memory)
    }

    suspend fun updateMemory(memory: MemoryEntity) {
        memoryDao.updateMemory(memory)
    }

    suspend fun deleteMemory(id: Long) {
        memoryDao.deleteMemoryById(id)
    }

    suspend fun clearAll() {
        memoryDao.clearAllMemories()
    }
}
