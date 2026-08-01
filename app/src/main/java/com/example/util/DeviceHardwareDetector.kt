package com.example.util

import android.app.ActivityManager
import android.content.Context
import android.os.Environment
import android.os.StatFs
import com.example.domain.model.AiModelInfo
import com.example.domain.model.ModelCategory

data class DeviceHardwareInfo(
    val totalRamMb: Int,
    val availableRamMb: Int,
    val cpuCores: Int,
    val totalStorageGb: Double,
    val freeStorageGb: Double,
    val tier: DeviceTier,
    val tierTitle: String,
    val tierDescription: String,
    val recommendedModelId: String,
    val suitableCategories: List<ModelCategory>
)

enum class DeviceTier {
    ENTRY_LEVEL, // < 3.5 GB RAM or <= 4 cores
    MID_RANGE,   // 3.5 GB - 6.5 GB RAM, 6-8 cores
    FLAGSHIP     // > 6.5 GB RAM, 8+ cores
}

object DeviceHardwareDetector {

    fun detect(context: Context): DeviceHardwareInfo {
        // RAM Detection
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager?.getMemoryInfo(memoryInfo)

        val totalRamMb = (memoryInfo.totalMem / (1024 * 1024)).toInt()
        val availableRamMb = (memoryInfo.availMem / (1024 * 1024)).toInt()

        // CPU Cores
        val cpuCores = Runtime.getRuntime().availableProcessors()

        // Storage Detection
        val stat = StatFs(Environment.getDataDirectory().path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong

        val totalStorageGb = (totalBlocks * blockSize).toDouble() / (1024.0 * 1024.0 * 1024.0)
        val freeStorageGb = (availableBlocks * blockSize).toDouble() / (1024.0 * 1024.0 * 1024.0)

        // Hardware Classification
        val tier = when {
            totalRamMb < 3600 || cpuCores <= 4 -> DeviceTier.ENTRY_LEVEL
            totalRamMb < 7000 -> DeviceTier.MID_RANGE
            else -> DeviceTier.FLAGSHIP
        }

        val (title, desc, recId, categories) = when (tier) {
            DeviceTier.ENTRY_LEVEL -> Quadruple(
                "Entry Level / Budget Hardware (${totalRamMb / 1024}GB RAM, ${cpuCores}-Cores)",
                "Recommended ultra-fast sub-1GB INT4 models for smooth zero-lag generation.",
                "qwen-2.5-0.5b-int4",
                listOf(ModelCategory.ULTRA_FAST)
            )
            DeviceTier.MID_RANGE -> Quadruple(
                "Mid-Range Hardware (${totalRamMb / 1024}GB RAM, ${cpuCores}-Cores)",
                "Optimal performance with 1GB-2GB INT4 models balancing accuracy & speed.",
                "minicpm-2b-int4",
                listOf(ModelCategory.ULTRA_FAST, ModelCategory.BALANCED)
            )
            DeviceTier.FLAGSHIP -> Quadruple(
                "Flagship / High Performance (${totalRamMb / 1024}GB RAM, ${cpuCores}-Cores)",
                "Full support for high quality 3B-7B models like MiniCPM 3.0, Mistral 7B, and Phi-3.",
                "minicpm-3-4b-int4",
                listOf(ModelCategory.ULTRA_FAST, ModelCategory.BALANCED, ModelCategory.HIGH_QUALITY)
            )
        }

        return DeviceHardwareInfo(
            totalRamMb = totalRamMb,
            availableRamMb = availableRamMb,
            cpuCores = cpuCores,
            totalStorageGb = Math.round(totalStorageGb * 10.0) / 10.0,
            freeStorageGb = Math.round(freeStorageGb * 10.0) / 10.0,
            tier = tier,
            tierTitle = title,
            tierDescription = desc,
            recommendedModelId = recId,
            suitableCategories = categories
        )
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
