package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val chatId: Long,
    val sender: String, // "user" or "assistant"
    val text: String,
    val modelId: String? = null,
    val tokensPerSecond: Double = 0.0,
    val tokenCount: Int = 0,
    val ttftMs: Long = 0L,
    val cpuThreads: Int = 4,
    val timestamp: Long = System.currentTimeMillis()
)
