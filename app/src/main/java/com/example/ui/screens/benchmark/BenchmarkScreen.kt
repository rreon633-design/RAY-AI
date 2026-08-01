package com.example.ui.screens.benchmark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ImmersiveCanvas
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
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
            TopAppBar(
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary
                            )
                        }
                    }
                },
                title = {
                    Column {
                        Text(
                            text = "CPU Performance Lab",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Test local ARM NEON & SIMD matrix math throughput",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ImmersiveSurface)
            )
        },
        containerColor = ImmersiveCanvas,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(ImmersiveCanvas)
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

