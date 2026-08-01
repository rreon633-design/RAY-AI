package com.example.domain.model

data class InferenceConfig(
    val cpuThreads: Int = 4,
    val contextWindow: Int = 2048,
    val quantization: String = "INT4 (q4_k_m)",
    val temperature: Float = 0.7f,
    val topK: Int = 40,
    val topP: Float = 0.9f,
    val repeatPenalty: Float = 1.1f,
    val systemPrompt: String = "You are an intelligent, helpful offline AI assistant running locally on the user's mobile device CPU. Be clear, concise, accurate, and direct."
)

data class CpuMetrics(
    val tokensPerSecond: Double = 0.0,
    val totalTokensGenerated: Int = 0,
    val ramUsedMb: Int = 0,
    val cpuUsagePercent: Int = 0,
    val timeToFirstTokenMs: Long = 0L,
    val inferenceTimeMs: Long = 0L,
    val isRunning: Boolean = false
)
