package com.example.data.repository

import com.example.data.local.ChatDao
import com.example.data.local.ChatEntity
import com.example.data.local.MessageEntity
import kotlinx.coroutines.flow.Flow

class ChatRepository(private val chatDao: ChatDao) {
    val allSessions: Flow<List<ChatEntity>> = chatDao.getAllSessions()

    fun getMessagesForChat(chatId: Long): Flow<List<MessageEntity>> = chatDao.getMessagesForChat(chatId)

    suspend fun createNewSession(title: String, modelId: String, modelName: String): Long {
        val session = ChatEntity(
            title = title,
            activeModelId = modelId,
            activeModelName = modelName
        )
        return chatDao.insertSession(session)
    }

    suspend fun getSessionById(sessionId: Long): ChatEntity? = chatDao.getSessionById(sessionId)

    suspend fun updateSessionModel(sessionId: Long, modelId: String, modelName: String) {
        chatDao.updateSessionModel(sessionId, modelId, modelName)
    }

    suspend fun deleteSession(sessionId: Long) {
        chatDao.deleteMessagesForChat(sessionId)
        chatDao.deleteSession(sessionId)
    }

    suspend fun saveMessage(message: MessageEntity): Long {
        return chatDao.insertMessage(message)
    }

    suspend fun deleteMessage(messageId: Long) {
        chatDao.deleteMessageById(messageId)
    }
}
