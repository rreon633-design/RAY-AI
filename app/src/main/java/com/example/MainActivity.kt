package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.ChatRepository
import com.example.data.repository.ModelRepository
import com.example.data.repository.SettingsRepository
import com.example.ui.navigation.AppNavBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.benchmark.BenchmarkScreen
import com.example.ui.screens.chat.ChatScreen
import com.example.ui.screens.models.ModelsScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.theme.LocalAiTheme
import com.example.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getInstance(applicationContext)
        val chatRepository = ChatRepository(db.chatDao())
        val modelRepository = ModelRepository(db.modelDao())
        val settingsRepository = SettingsRepository(db.settingsDao())

        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(ChatViewModel::class.java) -> ChatViewModel(chatRepository, modelRepository, settingsRepository) as T
                    modelClass.isAssignableFrom(ModelViewModel::class.java) -> ModelViewModel(modelRepository) as T
                    modelClass.isAssignableFrom(BenchmarkViewModel::class.java) -> BenchmarkViewModel() as T
                    modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(settingsRepository) as T
                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(factory = factory)
            val settingsState by settingsViewModel.uiState.collectAsState()

            LocalAiTheme(themeMode = settingsState.settings.themeMode) {
                var isSplashActive by remember { mutableStateOf(true) }

                Crossfade(
                    targetState = isSplashActive,
                    animationSpec = tween(700, easing = FastOutSlowInEasing),
                    label = "splash_transition"
                ) { showSplash ->
                    if (showSplash) {
                        SplashScreen(
                            onSplashFinished = { isSplashActive = false }
                        )
                    } else {
                        var currentRoute by remember { mutableStateOf(Screen.Chat.route) }

                        val chatViewModel: ChatViewModel = viewModel(factory = factory)
                        val modelViewModel: ModelViewModel = viewModel(factory = factory)
                        val benchmarkViewModel: BenchmarkViewModel = viewModel(factory = factory)

                        val chatState by chatViewModel.uiState.collectAsState()
                        val modelsState by modelViewModel.uiState.collectAsState()
                        val benchmarkState by benchmarkViewModel.uiState.collectAsState()

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = {
                                AppNavBar(
                                    currentRoute = currentRoute,
                                    onNavigate = { route -> currentRoute = route }
                                )
                            },
                            containerColor = Color.White
                        ) { innerPadding ->
                            val modifier = Modifier.padding(innerPadding)

                            when (currentRoute) {
                                Screen.Chat.route -> {
                                    ChatScreen(
                                        state = chatState,
                                        onSelectModel = chatViewModel::switchActiveModel,
                                        onSelectSession = chatViewModel::selectSession,
                                        onNewChat = { chatViewModel.createNewChat() },
                                        onDeleteSession = chatViewModel::deleteSession,
                                        onSendMessage = chatViewModel::sendMessage,
                                        onStopGeneration = chatViewModel::stopGeneration,
                                        onClearError = chatViewModel::clearErrorMessage,
                                        onNavigateToSettings = { currentRoute = Screen.Settings.route },
                                        onNavigateToModels = { currentRoute = Screen.Models.route },
                                        modifier = modifier
                                    )
                                }
                                Screen.Models.route -> {
                                    ModelsScreen(
                                        state = modelsState,
                                        onSelectCategory = modelViewModel::setCategoryFilter,
                                        onStartDownload = modelViewModel::startDownload,
                                        onDeleteModel = modelViewModel::cancelOrDeleteModel,
                                        onLoadModel = modelViewModel::loadModel,
                                        onUnloadModel = modelViewModel::unloadModel,
                                        onDetectHardware = modelViewModel::detectHardware,
                                        onBackClick = { currentRoute = Screen.Chat.route },
                                        modifier = modifier
                                    )
                                }
                                Screen.Benchmark.route -> {
                                    BenchmarkScreen(
                                        state = benchmarkState,
                                        onRunTest = benchmarkViewModel::runBenchmarkTest,
                                        onBackClick = { currentRoute = Screen.Chat.route },
                                        modifier = modifier
                                    )
                                }
                                Screen.Settings.route -> {
                                    SettingsScreen(
                                        state = settingsState,
                                        memoriesList = settingsViewModel.getMemoriesList(),
                                        onAddMemory = settingsViewModel::addMemory,
                                        onDeleteMemory = settingsViewModel::deleteMemory,
                                        onClearAllMemories = settingsViewModel::clearAllMemories,
                                        onSelectThemeMode = settingsViewModel::updateThemeMode,
                                        onDownloadAlertsChanged = settingsViewModel::updateDownloadAlertsEnabled,
                                        onDailyBriefChanged = settingsViewModel::updateDailyBriefEnabled,
                                        onUsageWarningsChanged = settingsViewModel::updateUsageWarningsEnabled,
                                        onSubmitBugReport = settingsViewModel::submitBugReport,
                                        onClearBugReportStatus = settingsViewModel::clearBugReportStatus,
                                        onUpdateCpuThreads = settingsViewModel::updateCpuThreads,
                                        onUpdateContextWindow = settingsViewModel::updateContextWindow,
                                        onUpdateTemperature = settingsViewModel::updateTemperature,
                                        onUpdateSystemPrompt = settingsViewModel::updateSystemPrompt,
                                        onBackClick = { currentRoute = Screen.Chat.route },
                                        modifier = modifier
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


