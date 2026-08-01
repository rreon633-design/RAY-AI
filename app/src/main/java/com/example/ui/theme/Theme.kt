package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AccentOrange,
    onPrimary = OnAccentOrange,
    primaryContainer = OrangeContainer,
    onPrimaryContainer = OnOrangeContainer,
    secondary = AccentOrange,
    onSecondary = OnAccentOrange,
    secondaryContainer = SunriseLightSurfaceVariant,
    onSecondaryContainer = TextPrimaryLight,
    tertiary = AccentOrange,
    background = SunriseLightCanvas,
    onBackground = TextPrimaryLight,
    surface = SunriseLightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = SunriseLightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight,
    outlineVariant = BorderSubtleLight
)

private val DarkColorScheme = darkColorScheme(
    primary = AccentOrange,
    onPrimary = OnAccentOrange,
    primaryContainer = OrangeContainer,
    onPrimaryContainer = OnOrangeContainer,
    secondary = AccentOrange,
    onSecondary = OnAccentOrange,
    secondaryContainer = SunriseDarkSurfaceVariant,
    onSecondaryContainer = TextPrimaryDark,
    tertiary = AccentOrange,
    background = SunriseDarkCanvas,
    onBackground = TextPrimaryDark,
    surface = SunriseDarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = SunriseDarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderDark,
    outlineVariant = BorderSubtleDark
)

@Composable
fun LocalAiTheme(
    themeMode: String = "LIGHT",
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        "DARK" -> true
        "LIGHT" -> false
        else -> isSystemInDarkTheme()
    }

    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}



