package com.example.domain.engine

import com.example.domain.model.AiModelInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.min

data class DownloadProgress(
    val modelId: String,
    val downloadedBytes: Long,
    val totalBytes: Long,
    val progressPercent: Float,
    val speedMbPerSec: Double,
    val etaSeconds: Int,
    val isCompleted: Boolean,
    val isPaused: Boolean = false,
    val error: String? = null
)

class ModelDownloader {

    fun downloadModelStream(
        modelInfo: AiModelInfo,
        startBytes: Long = 0L,
        maxRetries: Int = 3
    ): Flow<DownloadProgress> = flow {
        var currentBytes = startBytes
        val totalBytes = modelInfo.sizeBytes
        val chunkSize = 18_000_000L // Simulate ~18 MB download steps

        var lastTime = System.currentTimeMillis()
        var retryCount = 0

        while (currentBytes < totalBytes) {
            try {
                delay(350L) // Simulate network chunk tick
                currentBytes = min(totalBytes, currentBytes + chunkSize)

                val currentTime = System.currentTimeMillis()
                val timeDiffSec = (currentTime - lastTime) / 1000.0
                lastTime = currentTime

                val speedMbPerSec = if (timeDiffSec > 0) (chunkSize / (1024.0 * 1024.0)) / timeDiffSec else 15.0
                val remainingBytes = totalBytes - currentBytes
                val etaSeconds = if (speedMbPerSec > 0) ((remainingBytes / (1024.0 * 1024.0)) / speedMbPerSec).toInt() else 0
                val progressPercent = ((currentBytes.toDouble() / totalBytes) * 100).toFloat()

                emit(
                    DownloadProgress(
                        modelId = modelInfo.id,
                        downloadedBytes = currentBytes,
                        totalBytes = totalBytes,
                        progressPercent = progressPercent,
                        speedMbPerSec = Math.round(speedMbPerSec * 10.0) / 10.0,
                        etaSeconds = etaSeconds,
                        isCompleted = currentBytes >= totalBytes,
                        isPaused = false
                    )
                )
            } catch (e: Exception) {
                retryCount++
                if (retryCount > maxRetries) {
                    emit(
                        DownloadProgress(
                            modelId = modelInfo.id,
                            downloadedBytes = currentBytes,
                            totalBytes = totalBytes,
                            progressPercent = ((currentBytes.toDouble() / totalBytes) * 100).toFloat(),
                            speedMbPerSec = 0.0,
                            etaSeconds = 0,
                            isCompleted = false,
                            isPaused = true,
                            error = "Download failed after $maxRetries retries: ${e.message}"
                        )
                    )
                    break
                } else {
                    delay(1000L * retryCount) // Exponential backoff retry
                }
            }
        }
    }
}
