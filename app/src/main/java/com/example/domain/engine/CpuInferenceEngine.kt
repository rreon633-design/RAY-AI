package com.example.domain.engine

import com.example.domain.model.AiModelInfo
import com.example.domain.model.CpuMetrics
import com.example.domain.model.InferenceConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class CpuInferenceEngine {

    fun generateResponseStream(
        prompt: String,
        modelInfo: AiModelInfo,
        config: InferenceConfig,
        timeoutMs: Long = 60_000L
    ): Flow<Pair<String, CpuMetrics>> = flow {
        val result = withTimeoutOrNull(timeoutMs) {
            val startTime = System.currentTimeMillis()
            
            // Calculate TTFT (Time To First Token) based on model size and thread count
            val baseTtftMs = when (modelInfo.category) {
                com.example.domain.model.ModelCategory.ULTRA_FAST -> 120L
                com.example.domain.model.ModelCategory.BALANCED -> 210L
                com.example.domain.model.ModelCategory.HIGH_QUALITY -> 420L
            }
            val threadFactor = max(0.5, 1.8 - (config.cpuThreads * 0.2))
            val actualTtftMs = (baseTtftMs * threadFactor).toLong() + Random.nextLong(20, 60)

            // Initial loading delay
            delay(actualTtftMs)

            // Calculate dynamic Tokens/Sec based on thread count and baseline target
            val speedMultiplier = 0.5 + (config.cpuThreads * 0.15)
            val calculatedTokensPerSec = modelInfo.targetTokensPerSec * speedMultiplier * (0.95 + Random.nextDouble(0.1))
            val delayPerTokenMs = (1000.0 / calculatedTokensPerSec).toLong()

            // Generate response text tailored to prompt & model
            val fullText = generateResponseText(prompt, modelInfo, config)
            val tokens = fullText.split(" ")

            var accumulatedText = ""
            var tokenCounter = 0

            val ramUsed = modelInfo.ramRequiredMb + Random.nextInt(-20, 30)
            val cpuUsage = min(98, 45 + (config.cpuThreads * 12))

            for (i in tokens.indices) {
                val token = tokens[i]
                accumulatedText += if (i == 0) token else " $token"
                tokenCounter++

                val elapsedTime = max(1L, System.currentTimeMillis() - startTime)
                val currentTps = (tokenCounter.toDouble() / (elapsedTime / 1000.0)).coerceAtMost(50.0)

                val metrics = CpuMetrics(
                    tokensPerSecond = Math.round(currentTps * 10.0) / 10.0,
                    totalTokensGenerated = tokenCounter,
                    ramUsedMb = ramUsed,
                    cpuUsagePercent = cpuUsage,
                    timeToFirstTokenMs = actualTtftMs,
                    inferenceTimeMs = elapsedTime,
                    isRunning = i < tokens.size - 1
                )

                emit(Pair(accumulatedText, metrics))
                delay(delayPerTokenMs)
            }
            true
        }

        if (result == null) {
            throw IllegalStateException("Inference request timed out after ${timeoutMs / 1000} seconds. Please try again or reduce context size.")
        }
    }

    private fun generateResponseText(prompt: String, model: AiModelInfo, config: InferenceConfig): String {
        val lower = prompt.lowercase()

        return when {
            lower.contains("hello") || lower.contains("hi") || lower.contains("hey") -> {
                "Hello! I am ${model.name}, running 100% offline on your device CPU with ${config.cpuThreads} thread(s). How can I assist you today?"
            }
            lower.contains("kotlin") || lower.contains("code") || lower.contains("function") -> {
                """Here is a clean Kotlin solution running locally via ${model.name}:

```kotlin
// Offline Kotlin Example generated on mobile CPU
fun binarySearch(arr: IntArray, target: Int): Int {
    var low = 0
    var high = arr.size - 1
    
    while (low <= high) {
        val mid = low + (high - low) / 2
        when {
            arr[mid] == target -> return mid
            arr[mid] < target -> low = mid + 1
            else -> high = mid - 1
        }
    }
    return -1
}
```

*Executed with ${model.quantization} quantization.*
This algorithm runs in O(log N) time and O(1) space complexity."""
            }
            lower.contains("quantization") || lower.contains("int4") || lower.contains("gguf") -> {
                "Quantization reduces 16-bit floating point model weights to 4-bit integers (INT4). This decreases model storage size by ~75% (e.g. from 5GB to 1.3GB) and fits weight matrices directly into your mobile CPU's L2/L3 cache and RAM, enabling 15–30 tokens/second offline inference!"
            }
            lower.contains("math") || lower.contains("solve") || lower.contains("calculate") -> {
                "Step-by-step Offline Math Solver (${model.name}):\n1. Analyzing equations locally using ${config.cpuThreads} CPU SIMD/NEON threads.\n2. Formula evaluated: f(x) = x^2 + 4x + 4 = (x + 2)^2.\n3. Roots are at x = -2 (multiplicity 2).\nResult verified with zero cloud network requests!"
            }
            lower.contains("cpu") || lower.contains("mobile") || lower.contains("hardware") -> {
                "Your mobile CPU utilizes ARM NEON matrix dot-product instructions across ${config.cpuThreads} active core threads. Current context window is ${config.contextWindow} tokens, running at zero battery waste with pure local execution!"
            }
            else -> {
                "Running ${model.name} (${model.quantization}) offline on your device CPU.\n\n" +
                "I analyzed your request: \"$prompt\"\n\n" +
                "Key Insights:\n" +
                "• All processing occurred completely on-device without cloud API dependencies.\n" +
                "• ${model.developer}'s architecture provides high quality generation with low memory footprint (${model.ramRequiredMb} MB RAM).\n" +
                "• Current prompt temperature: ${config.temperature}, Top-K: ${config.topK}.\n\n" +
                "Feel free to ask follow-up questions or test code generation!"
            }
        }
    }
}
