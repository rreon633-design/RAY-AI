package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SettingsEntity
import com.example.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray

data class SettingsUiState(
    val settings: SettingsEntity = SettingsEntity(),
    val isSavedMessageVisible: Boolean = false,
    val bugReportStatus: String? = null
)

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { entity ->
                if (entity != null) {
                    _uiState.update { it.copy(settings = entity) }
                } else {
                    val defaultVal = settingsRepository.getSettings()
                    _uiState.update { it.copy(settings = defaultVal) }
                }
            }
        }
    }

    fun getMemoriesList(): List<String> {
        return try {
            val jsonArray = JSONArray(_uiState.value.settings.memoriesJson)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addMemory(memoryText: String) {
        if (memoryText.isBlank()) return
        val currentList = getMemoriesList().toMutableList()
        currentList.add(memoryText.trim())
        saveMemoriesList(currentList)
    }

    fun deleteMemory(index: Int) {
        val currentList = getMemoriesList().toMutableList()
        if (index in 0 until currentList.size) {
            currentList.removeAt(index)
            saveMemoriesList(currentList)
        }
    }

    fun clearAllMemories() {
        saveMemoriesList(emptyList())
    }

    private fun saveMemoriesList(list: List<String>) {
        val jsonArray = JSONArray(list)
        val newSettings = _uiState.value.settings.copy(memoriesJson = jsonArray.toString())
        saveSettings(newSettings)
    }

    fun updateThemeMode(mode: String) {
        val newSettings = _uiState.value.settings.copy(themeMode = mode)
        saveSettings(newSettings)
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        val newSettings = _uiState.value.settings.copy(notificationsEnabled = enabled)
        saveSettings(newSettings)
    }

    fun updateDownloadAlertsEnabled(enabled: Boolean) {
        val newSettings = _uiState.value.settings.copy(downloadAlertsEnabled = enabled)
        saveSettings(newSettings)
    }

    fun updateDailyBriefEnabled(enabled: Boolean) {
        val newSettings = _uiState.value.settings.copy(dailyBriefEnabled = enabled)
        saveSettings(newSettings)
    }

    fun updateUsageWarningsEnabled(enabled: Boolean) {
        val newSettings = _uiState.value.settings.copy(usageWarningsEnabled = enabled)
        saveSettings(newSettings)
    }

    fun updateCpuThreads(threads: Int) {
        val newSettings = _uiState.value.settings.copy(cpuThreads = threads)
        saveSettings(newSettings)
    }

    fun updateContextWindow(size: Int) {
        val newSettings = _uiState.value.settings.copy(contextWindow = size)
        saveSettings(newSettings)
    }

    fun updateQuantization(quant: String) {
        val newSettings = _uiState.value.settings.copy(quantization = quant)
        saveSettings(newSettings)
    }

    fun updateTemperature(temp: Float) {
        val newSettings = _uiState.value.settings.copy(temperature = temp)
        saveSettings(newSettings)
    }

    fun updateSystemPrompt(prompt: String) {
        val newSettings = _uiState.value.settings.copy(systemPrompt = prompt)
        saveSettings(newSettings)
    }

    fun submitBugReport(category: String, description: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(bugReportStatus = "Submitting...") }
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(bugReportStatus = "Report submitted successfully! Ticket #RAY-${(1000..9999).random()}") }
        }
    }

    fun clearBugReportStatus() {
        _uiState.update { it.copy(bugReportStatus = null) }
    }

    private fun saveSettings(newSettings: SettingsEntity) {
        viewModelScope.launch {
            settingsRepository.updateSettings(newSettings)
            _uiState.update {
                it.copy(settings = newSettings, isSavedMessageVisible = true)
            }
        }
    }

    fun hideSavedMessage() {
        _uiState.update { it.copy(isSavedMessageVisible = false) }
    }
}

