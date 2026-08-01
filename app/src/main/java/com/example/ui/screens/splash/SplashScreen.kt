package com.example.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeobrutalCard
import com.example.ui.components.NeobrutalProgressBar
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Logo scale (Overshoot effect using a high-fidelity spring)
    val scale = remember { Animatable(0f) }
    
    // 2. Text and UI elements fade-in opacity
    val opacity = remember { Animatable(0f) }
    
    // 3. Progress bar animation (0f to 1f)
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // High-tension spring overshoot for that tactile punchy neobrutal entry
        scale.animateTo(
            targetValue = 1.15f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(200)
        opacity.animateTo(
            targetValue = 1f,
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        // Fast, smooth loading simulation
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(2000, easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1.0f))
        )
        delay(400)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White), // Forced white background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            // Overshooting Neobrutalist Logo Container
            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .size(110.dp)
            ) {
                NeobrutalCard(
                    modifier = Modifier.fillMaxSize(),
                    backgroundColor = Color(0xFFFCDF46), // Vibrant yellow base
                    borderColor = Color.Black,
                    shadowColor = Color.Black,
                    borderWidth = 3.dp,
                    shadowOffset = 8.dp,
                    cornerRadius = 0.dp // Strict crisp rectangular neobrutal shape
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = "RAY AI Logo",
                            tint = Color.Black,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Brand Typography
            Text(
                text = "RAY AI",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(opacity.value)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "PRIVATE AI. ON-DEVICE ENGINE.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(opacity.value)
            )
        }

        // Bottom Progress Indicator with strict Neobrutalist design
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 72.dp)
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .alpha(opacity.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "INITIALIZING NATIVE ENGINES...",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stark neobrutalist progress bar
            NeobrutalProgressBar(
                progress = progress.value,
                barHeight = 18.dp,
                progressColor = Color(0xFF7DD3FC), // Glacier Light Blue progress fill
                backgroundColor = Color.White,
                borderColor = Color.Black,
                borderWidth = 3.dp
            )
        }
    }
}
