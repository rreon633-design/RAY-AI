package com.example.data.repository

import com.example.data.local.ChatDao
import com.example.data.local.ChatEntity
import com.example.data.local.MessageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeChatDao : ChatDao {
    val sessions = mutableListOf<ChatEntity>()
    val messages = mutableListOf<MessageEntity>()
    var nextSessionId = 1L
    var nextMessageId = 1L

    override fun getAllSessions(): Flow<List<ChatEntity>> = flowOf(sessions)
    
    override fun getMessagesForChat(chatId: Long): Flow<List<MessageEntity>> = flowOf(messages.filter { it.chatId == chatId })

    override suspend fun insertSession(session: ChatEntity): Long {
        val newSession = session.copy(id = nextSessionId++)
        sessions.add(newSession)
        return newSession.id
    }

    override suspend fun getSessionById(sessionId: Long): ChatEntity? = sessions.find { it.id == sessionId }

    override suspend fun updateSessionModel(sessionId: Long, modelId: String, modelName: String, timestamp: Long) {
        val index = sessions.indexOfFirst { it.id == sessionId }
        if (index != -1) {
            sessions[index] = sessions[index].copy(activeModelId = modelId, activeModelName = modelName, updatedAt = timestamp)
        }
    }

    override suspend fun deleteSession(sessionId: Long) {
        sessions.removeIf { it.id == sessionId }
    }

    override suspend fun insertMessage(message: MessageEntity): Long {
        val newMessage = message.copy(id = nextMessageId++)
        messages.add(newMessage)
        return newMessage.id
    }

    override suspend fun deleteMessageById(messageId: Long) {
        messages.removeIf { it.id == messageId }
    }

    override suspend fun deleteMessagesForChat(chatId: Long) {
        messages.removeIf { it.chatId == chatId }
    }
}
