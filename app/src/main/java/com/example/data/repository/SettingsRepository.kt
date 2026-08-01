package com.example.data.repository

import com.example.data.local.SettingsDao
import com.example.data.local.SettingsEntity
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val settingsDao: SettingsDao) {
    val settingsFlow: Flow<SettingsEntity?> = settingsDao.getSettingsFlow()

    suspend fun getSettings(): SettingsEntity {
        val current = settingsDao.getSettings()
        if (current == null) {
            val defaultSettings = SettingsEntity()
            settingsDao.saveSettings(defaultSettings)
            return defaultSettings
        }
        return current
    }

    suspend fun updateSettings(settings: SettingsEntity) {
        settingsDao.saveSettings(settings)
    }
}
