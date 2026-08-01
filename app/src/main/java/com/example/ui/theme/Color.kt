package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Sunrise Light Theme Palette
val SunriseLightCanvas = Color(0xFFFFFFFF)          // Warm Off-White / Pure White Canvas
val SunriseLightSurface = Color(0xFFFFFFFF)         // Pure White Card & Bar Surface
val SunriseLightSurfaceVariant = Color(0xFFFFFFFF)  // Always white for pristine neobrutalist contrast
val SunriseLightBottomBar = Color(0xFFFFFFFF)       // Bottom navigation surface

val AccentOrange = Color(0xFF006686)               // Glacier Blue/Deep Cyan Accent
val OnAccentOrange = Color(0xFFFFFFFF)             // White text on Accent
val OrangeContainer = Color(0xFF7DD3FC)            // Glacier light-blue container
val OnOrangeContainer = Color(0xFF001E2B)          // Deep blue on light-blue container

val TextPrimaryLight = Color(0xFF1B1B1B)           // Stark charcoal/black text
val TextSecondaryLight = Color(0xFF3F484E)         // Charcoal grey text
val TextMutedLight = Color(0xFF6F787E)             // Muted grey text

val BorderLight = Color(0xFF1B1B1B)                // Bold, thick border color
val BorderSubtleLight = Color(0xFF1B1B1B)          // Bold inner dividers

// Sunrise Dark Theme Palette (Adapted to keep background white)
val SunriseDarkCanvas = Color(0xFFFFFFFF)           // Forces white background as requested
val SunriseDarkSurface = Color(0xFFFFFFFF)          // White surface
val SunriseDarkSurfaceVariant = Color(0xFFFFFFFF)   // White variant
val SunriseDarkBottomBar = Color(0xFFFFFFFF)        // White bottom bar

val TextPrimaryDark = Color(0xFF1B1B1B)            // Keep dark text
val TextSecondaryDark = Color(0xFF3F484E)
val TextMutedDark = Color(0xFF6F787E)

val BorderDark = Color(0xFF1B1B1B)                 // Keep bold border
val BorderSubtleDark = Color(0xFF1B1B1B)

// Default Aliases (for backward compatibility)
val ImmersiveCanvas = Color(0xFFFFFFFF)
val ImmersiveSurface = Color(0xFFFFFFFF)
val ImmersiveSurfaceVariant = Color(0xFFFFFFFF)
val ImmersiveBottomBar = Color(0xFFFFFFFF)

val AccentPurple = AccentOrange
val OnAccentPurple = OnAccentOrange
val PurpleContainer = OrangeContainer
val OnPurpleContainer = OnOrangeContainer

val TextPrimary = TextPrimaryLight
val TextSecondary = TextSecondaryLight
val TextMuted = TextMutedLight

val ImmersiveBorder = Color(0xFF1B1B1B)
val ImmersiveBorderSubtle = Color(0xFF1B1B1B)

val SuccessGreen = Color(0xFF7FB77E)               // Soft Amber-Green
val WarningAmber = Color(0xFFFCDF46)               // Vibrant Neobrutalist Yellow
val ErrorRed = Color(0xFFBA1A1A)                   // Stark Coral-Red
val AccentCyan = Color(0xFF7DD3FC)                 // Glacier Light Blue
val AccentGreen = Color(0xFF7FB77E)                // Soft Green
val CardBorder = Color(0xFF1B1B1B)
val ImmersiveCardBg = Color(0xFFFFFFFF)
