package com.example.ui.screens.benchmark

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeobrutalIconButton
import com.example.ui.viewmodel.BenchmarkUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BenchmarkScreen(
    state: BenchmarkUiState,
    onRunTest: (Int) -> Unit,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            // High-contrast Neobrutalist Top Bar with thick bottom border
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(width = 2.dp, color = Color.Black)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (onBackClick != null) {
                        NeobrutalIconButton(
                            onClick = onBackClick,
                            backgroundColor = Color.White,
                            shadowOffset = 2.dp,
                            borderWidth = 1.5.dp,
                            isCircular = false,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "PERFORMANCE LAB",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black
                        )
                        Text(
                            text = "MEASURE LOCAL CPU MATRIX MATH SPEED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Gray
                        )
                    }
                }
            }
        },
        containerColor = Color.White, // Always make background white
        modifier = modifier
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White) // Forced white background
        ) {
            item {
                HardwareSpecCard(spec = state.spec)
            }

            item {
                BenchmarkRunCard(
                    isTesting = state.isTesting,
                    progressPercent = state.testProgressPercent,
                    stageText = state.testStageText,
                    lastResult = state.lastResult,
                    onRunTest = onRunTest
                )
            }
        }
    }
}
