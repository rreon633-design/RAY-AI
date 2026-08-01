package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Chat : Screen("chat", "Chat", Icons.Default.ChatBubbleOutline)
    object Models : Screen("models", "Models Hub", Icons.Default.DownloadForOffline)
    object Benchmark : Screen("benchmark", "CPU Lab", Icons.Default.Speed)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    companion object {
        val items: List<Screen> get() = listOf(Chat, Models, Settings)
    }
}

