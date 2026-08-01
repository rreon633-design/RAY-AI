package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloaded_models")
data class ModelEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val isDownloaded: Boolean = false,
    val isDownloading: Boolean = false,
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = 0L,
    val downloadProgressPercent: Float = 0f,
    val isActiveModel: Boolean = false,
    val lastUsedAt: Long = 0L
)
