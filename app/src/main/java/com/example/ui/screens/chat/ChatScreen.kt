package com.example.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.ImmersiveCanvas
import com.example.ui.viewmodel.ChatUiState
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    state: ChatUiState,
    onSelectModel: (com.example.domain.model.AiModelInfo) -> Unit,
    onSelectSession: (Long) -> Unit,
    onNewChat: () -> Unit,
    onDeleteSession: (Long) -> Unit,
    onSendMessage: (String) -> Unit,
    onStopGeneration: () -> Unit,
    onClearError: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToModels: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { errorMsg ->
            val result = snackbarHostState.showSnackbar(
                message = errorMsg,
                actionLabel = "Dismiss",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed || result == SnackbarResult.Dismissed) {
                onClearError()
            }
        }
    }

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ChatDrawerSheet(
                sessions = state.sessions,
                currentSessionId = state.currentSessionId,
                onSelectSession = { id ->
                    onSelectSession(id)
                    scope.launch { drawerState.close() }
                },
                onNewChat = {
                    onNewChat()
                    scope.launch { drawerState.close() }
                },
                onDeleteSession = onDeleteSession,
                onNavigateToSettings = {
                    scope.launch { drawerState.close() }
                    onNavigateToSettings()
                },
                onNavigateToModels = {
                    scope.launch { drawerState.close() }
                    onNavigateToModels()
                }
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                ChatHeaderBar(
                    activeModel = state.activeModel,
                    status = state.modelStatus,
                    onSelectModel = onSelectModel,
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onNewChat = onNewChat
                )
            },
            bottomBar = {
                ChatInputBar(
                    isGenerating = state.isGenerating,
                    inputText = inputText,
                    onInputTextChange = { inputText = it },
                    onSendMessage = { message ->
                        onSendMessage(message)
                        inputText = ""
                    },
                    onStopGeneration = onStopGeneration
                )
            },
            contentWindowInsets = WindowInsets.ime,
            containerColor = ImmersiveCanvas
        ) { padding ->
            ChatMessageList(
                messages = state.messages,
                isGenerating = state.isGenerating,
                currentStreamingText = state.currentStreamingText,
                liveMetrics = state.liveMetrics,
                activeModel = state.activeModel,
                onSendMessage = { prompt ->
                    onSendMessage(prompt)
                    inputText = ""
                },
                onEditPrompt = { promptToEdit ->
                    inputText = promptToEdit
                },
                modifier = Modifier
                    .padding(padding)
                    .background(ImmersiveCanvas)
            )
        }
    }
}


