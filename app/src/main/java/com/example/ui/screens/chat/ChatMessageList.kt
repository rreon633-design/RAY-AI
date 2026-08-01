package com.example.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MessageEntity
import com.example.domain.model.CpuMetrics
import com.example.ui.theme.AccentPurple

import com.example.domain.model.AiModelInfo

@Composable
fun ChatMessageList(
    messages: List<MessageEntity>,
    isGenerating: Boolean,
    currentStreamingText: String,
    liveMetrics: CpuMetrics,
    activeModel: AiModelInfo,
    onSendMessage: (String) -> Unit,
    onEditPrompt: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, currentStreamingText.length) {
        if (messages.isNotEmpty() || currentStreamingText.isNotEmpty()) {
            listState.animateScrollToItem((messages.size + if (isGenerating) 1 else 0))
        }
    }

    if (messages.isEmpty() && !isGenerating) {
        EmptyChatWelcomeView(
            activeModel = activeModel,
            onSelectPrompt = onSendMessage,
            modifier = modifier
        )
    } else {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatMessageItem(message = msg, onEditPrompt = onEditPrompt)
            }

            if (isGenerating) {
                item {
                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        val streamingMsg = MessageEntity(
                            id = -1,
                            chatId = -1,
                            sender = "assistant",
                            text = if (currentStreamingText.isBlank()) "Initializing CPU threads & loading INT4 tensors..." else currentStreamingText,
                            tokensPerSecond = liveMetrics.tokensPerSecond,
                            tokenCount = liveMetrics.totalTokensGenerated,
                            cpuThreads = 4
                        )
                        ChatMessageItem(message = streamingMsg)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                        ) {
                            // Neobrutalist active status dot
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFFF4D03F))
                                    .border(1.5.dp, Color.Black)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "GENERATING | SPEED: ${liveMetrics.tokensPerSecond} t/s | RAM: ${liveMetrics.ramUsedMb} MB | CPU: ${liveMetrics.cpuUsagePercent}%",
                                fontSize = 10.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

