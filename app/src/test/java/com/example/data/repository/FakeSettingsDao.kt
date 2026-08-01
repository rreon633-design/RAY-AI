package com.example.data.repository

import com.example.data.local.SettingsDao
import com.example.data.local.SettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSettingsDao : SettingsDao {
    var settings: SettingsEntity? = null

    override fun getSettingsFlow(): Flow<SettingsEntity?> = flowOf(settings)

    override suspend fun getSettings(): SettingsEntity? = settings

    override suspend fun saveSettings(settings: SettingsEntity) {
        this.settings = settings
    }
}
