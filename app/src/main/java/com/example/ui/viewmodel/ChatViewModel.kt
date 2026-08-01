package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ChatEntity
import com.example.data.local.MessageEntity
import com.example.data.repository.ChatRepository
import com.example.data.repository.ModelRepository
import com.example.data.repository.SettingsRepository
import com.example.domain.engine.CpuInferenceEngine
import com.example.domain.model.AiModelInfo
import com.example.domain.model.CpuMetrics
import com.example.domain.model.InferenceConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class ModelStatus {
    READY,
    DOWNLOADING,
    PROCESSING
}

data class ChatUiState(
    val sessions: List<ChatEntity> = emptyList(),
    val currentSessionId: Long? = null,
    val currentSession: ChatEntity? = null,
    val messages: List<MessageEntity> = emptyList(),
    val activeModel: AiModelInfo = AiModelInfo.CATALOG[0],
    val config: InferenceConfig = InferenceConfig(),
    val isGenerating: Boolean = false,
    val isModelDownloading: Boolean = false,
    val isModelReady: Boolean = true,
    val currentStreamingText: String = "",
    val liveMetrics: CpuMetrics = CpuMetrics(),
    val errorMessage: String? = null
) {
    val modelStatus: ModelStatus
        get() = when {
            isGenerating -> ModelStatus.PROCESSING
            isModelDownloading -> ModelStatus.DOWNLOADING
            else -> ModelStatus.READY
        }
}

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val modelRepository: ModelRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val inferenceEngine = CpuInferenceEngine()
    private var generationJob: Job? = null

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            modelRepository.initializeDefaultModels()
            loadSessions()
            observeSettings()
            observeDownloadedModels()
        }
    }

    private fun observeDownloadedModels() {
        viewModelScope.launch {
            modelRepository.downloadedModels.collect { entities ->
                val activeModelId = _uiState.value.activeModel.id
                val modelEntity = entities.find { it.id == activeModelId }
                val isDownloading = modelEntity?.isDownloading == true
                val isReady = modelEntity?.isDownloaded == true || modelEntity == null
                _uiState.update {
                    it.copy(
                        isModelDownloading = isDownloading,
                        isModelReady = isReady
                    )
                }
            }
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                if (settings != null) {
                    val model = AiModelInfo.CATALOG.find { it.id == settings.selectedModelId }
                        ?: AiModelInfo.CATALOG[0]
                    _uiState.update {
                        it.copy(
                            activeModel = model,
                            config = InferenceConfig(
                                cpuThreads = settings.cpuThreads,
                                contextWindow = settings.contextWindow,
                                quantization = settings.quantization,
                                temperature = settings.temperature,
                                topK = settings.topK,
                                topP = settings.topP,
                                systemPrompt = settings.systemPrompt
                            )
                        )
                    }
                }
            }
        }
    }

    private fun loadSessions() {
        viewModelScope.launch {
            chatRepository.allSessions.collect { sessionsList ->
                _uiState.update { currentState ->
                    val activeId = currentState.currentSessionId ?: sessionsList.firstOrNull()?.id
                    val activeSession = sessionsList.find { it.id == activeId }
                    currentState.copy(
                        sessions = sessionsList,
                        currentSessionId = activeId,
                        currentSession = activeSession
                    )
                }

                if (_uiState.value.currentSessionId != null) {
                    loadMessagesForChat(_uiState.value.currentSessionId!!)
                } else if (sessionsList.isEmpty()) {
                    createDefaultSeedChat()
                }
            }
        }
    }

    fun selectSession(sessionId: Long) {
        _uiState.update { it.copy(currentSessionId = sessionId) }
        loadMessagesForChat(sessionId)
    }

    private fun loadMessagesForChat(chatId: Long) {
        viewModelScope.launch {
            chatRepository.getMessagesForChat(chatId).collect { msgs ->
                _uiState.update { it.copy(messages = msgs) }
            }
        }
    }

    fun createNewChat(title: String = "New Offline Chat") {
        viewModelScope.launch {
            val activeModel = _uiState.value.activeModel
            val newId = chatRepository.createNewSession(
                title = title,
                modelId = activeModel.id,
                modelName = activeModel.name
            )
            selectSession(newId)
        }
    }

    fun createDefaultSeedChat() {
        viewModelScope.launch {
            val activeModel = _uiState.value.activeModel
            val newId = chatRepository.createNewSession(
                title = "Neobrutalism Explained",
                modelId = activeModel.id,
                modelName = activeModel.name
            )
            // Seed 1 (assistant)
            chatRepository.saveMessage(
                MessageEntity(
                    chatId = newId,
                    sender = "assistant",
                    text = "Hello! I'm here to help you understand **Neobrutalism** in modern UI design. Essentially, it's a rejection of the soft, minimalist 'clean' aesthetics of the last decade.\n\nKey features include high-contrast colors, thick black borders (strokes), hard shadows with no blur, and a focus on raw, geometric shapes. It's built to feel tactile and honest.",
                    modelId = activeModel.id,
                    cpuThreads = 4,
                    tokenCount = 54,
                    tokensPerSecond = 12.5
                )
            )
            // Seed 2 (user)
            chatRepository.saveMessage(
                MessageEntity(
                    chatId = newId,
                    sender = "user",
                    text = "That sounds bold. Why is it becoming popular again for AI interfaces?",
                    modelId = activeModel.id,
                    cpuThreads = 4
                )
            )
            // Seed 3 (assistant)
            chatRepository.saveMessage(
                MessageEntity(
                    chatId = newId,
                    sender = "assistant",
                    text = "Great question! Neobrutalism gives AI a **tangible presence**. Because AI can feel ethereal or invisible, giving it a \"boxy,\" physical-looking interface makes it feel more like a reliable tool.\n\n- **Contrast**: Makes complex AI data readable.\n- **Geometry**: Implies logic and structure.\n\nTake a look at this structural study of neobrutalist components I've visualized for you:",
                    modelId = activeModel.id,
                    cpuThreads = 4,
                    tokenCount = 42,
                    tokensPerSecond = 14.1
                )
            )
            selectSession(newId)
        }
    }

    fun switchActiveModel(model: AiModelInfo) {
        viewModelScope.launch {
            _uiState.update { it.copy(activeModel = model) }
            val session = _uiState.value.currentSession
            if (session != null) {
                chatRepository.updateSessionModel(session.id, model.id, model.name)
            }
            val currentSettings = settingsRepository.getSettings()
            settingsRepository.updateSettings(currentSettings.copy(selectedModelId = model.id))
        }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank() || _uiState.value.isGenerating) return

        val chatId = _uiState.value.currentSessionId ?: return
        val activeModel = _uiState.value.activeModel
        val config = _uiState.value.config

        viewModelScope.launch {
            // Save User Message
            val userMsg = MessageEntity(
                chatId = chatId,
                sender = "user",
                text = userText.trim(),
                modelId = activeModel.id,
                cpuThreads = config.cpuThreads
            )
            chatRepository.saveMessage(userMsg)

            // Start Assistant Generation
            _uiState.update {
                it.copy(
                    isGenerating = true,
                    currentStreamingText = "",
                    liveMetrics = CpuMetrics(isRunning = true)
                )
            }

            generationJob = launch {
                try {
                    inferenceEngine.generateResponseStream(
                        prompt = userText,
                        modelInfo = activeModel,
                        config = config
                    ).collect { (streamedText, metrics) ->
                        _uiState.update {
                            it.copy(
                                currentStreamingText = streamedText,
                                liveMetrics = metrics
                            )
                        }
                    }

                    // On Stream Complete -> Save Assistant Message
                    val finalMetrics = _uiState.value.liveMetrics
                    val finalStreamText = _uiState.value.currentStreamingText

                    val assistantMsg = MessageEntity(
                        chatId = chatId,
                        sender = "assistant",
                        text = finalStreamText,
                        modelId = activeModel.id,
                        tokensPerSecond = finalMetrics.tokensPerSecond,
                        tokenCount = finalMetrics.totalTokensGenerated,
                        ttftMs = finalMetrics.timeToFirstTokenMs,
                        cpuThreads = config.cpuThreads
                    )
                    chatRepository.saveMessage(assistantMsg)

                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = e.message) }
                } finally {
                    _uiState.update {
                        it.copy(
                            isGenerating = false,
                            currentStreamingText = "",
                            liveMetrics = CpuMetrics(isRunning = false)
                        )
                    }
                }
            }
        }
    }

    fun stopGeneration() {
        generationJob?.cancel()
        _uiState.update {
            it.copy(
                isGenerating = false,
                currentStreamingText = "",
                liveMetrics = CpuMetrics(isRunning = false)
            )
        }
    }

    fun deleteSession(sessionId: Long) {
        viewModelScope.launch {
            chatRepository.deleteSession(sessionId)
            if (_uiState.value.currentSessionId == sessionId) {
                _uiState.update { it.copy(currentSessionId = null) }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
