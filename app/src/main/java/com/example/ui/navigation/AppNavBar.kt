package com.example.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBottomBar
import com.example.ui.theme.OnAccentPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun AppNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = ImmersiveBottomBar,
        tonalElevation = 4.dp
    ) {
        Screen.items.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(screen.route) },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OnAccentPurple,
                    selectedTextColor = AccentPurple,
                    indicatorColor = AccentPurple,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}

