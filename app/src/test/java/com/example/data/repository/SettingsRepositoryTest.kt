package com.example.data.repository

import com.example.data.local.SettingsEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class SettingsRepositoryTest {

    private lateinit var fakeDao: FakeSettingsDao
    private lateinit var settingsRepository: SettingsRepository

    @Before
    fun setup() {
        fakeDao = FakeSettingsDao()
        settingsRepository = SettingsRepository(fakeDao)
    }

    @Test
    fun `getSettings returns existing settings if present`() = runTest {
        val existingSettings = SettingsEntity(id = 1, themeMode = "DARK", systemPrompt = "Hello")
        fakeDao.settings = existingSettings
        val result = settingsRepository.getSettings()
        assertEquals(existingSettings, result)
    }

    @Test
    fun `getSettings creates and returns default settings if null`() = runTest {
        val result = settingsRepository.getSettings()
        assertNotNull(result)
        assertEquals(result, fakeDao.settings)
    }

    @Test
    fun `updateSettings calls saveSettings`() = runTest {
        val settings = SettingsEntity(id = 1, themeMode = "LIGHT", systemPrompt = "Test")
        settingsRepository.updateSettings(settings)
        assertEquals(settings, fakeDao.settings)
    }
}
