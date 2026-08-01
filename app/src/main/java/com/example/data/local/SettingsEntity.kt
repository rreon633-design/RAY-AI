package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class SettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val cpuThreads: Int = 4,
    val contextWindow: Int = 2048,
    val quantization: String = "INT4 (q4_k_m)",
    val temperature: Float = 0.7f,
    val topK: Int = 40,
    val topP: Float = 0.9f,
    val systemPrompt: String = "You are an intelligent, helpful offline AI assistant running locally on the user's mobile device CPU. Be clear, concise, accurate, and direct.",
    val selectedModelId: String = "qwen-2.5-0.5b-int4",
    val themeMode: String = "LIGHT",
    val notificationsEnabled: Boolean = true,
    val downloadAlertsEnabled: Boolean = true,
    val dailyBriefEnabled: Boolean = false,
    val usageWarningsEnabled: Boolean = true,
    val memoriesJson: String = "[\"User prefers clean, responsive web code\", \"User prefers concise explanations\", \"Default theme is Sunrise Light\"]"
)

