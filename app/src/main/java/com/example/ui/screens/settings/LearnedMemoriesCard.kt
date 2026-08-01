package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeobrutalButton
import com.example.ui.components.NeobrutalCard

@Composable
fun LearnedMemoriesCard(
    memories: List<String>,
    onDeleteMemory: (Int) -> Unit,
    onClearAllMemories: () -> Unit,
    modifier: Modifier = Modifier
) {
    NeobrutalCard(
        backgroundColor = Color(0xFF75C2F6), // Sky Blue background matching mockup
        shadowOffset = 6.dp,
        borderWidth = 2.5.dp,
        cornerRadius = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Title and Clear All button in a Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Learned",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFF1B4F72) // Dark Blue title
                    )
                    Text(
                        text = "Memories",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFF1B4F72)
                    )
                }

                // White "CLEAR ALL" button
                NeobrutalButton(
                    onClick = onClearAllMemories,
                    backgroundColor = Color.White,
                    borderWidth = 2.dp,
                    shadowOffset = 0.dp,
                    modifier = Modifier.width(100.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "CLEAR",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Black
                            )
                            Text(
                                text = "ALL",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Stack of Memories
            if (memories.isEmpty()) {
                NeobrutalCard(
                    backgroundColor = Color.White,
                    shadowOffset = 3.dp,
                    borderWidth = 2.dp,
                    cornerRadius = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NO MEMORIES STORED",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    memories.forEachIndexed { index, memory ->
                        NeobrutalCard(
                            backgroundColor = Color.White,
                            shadowOffset = 4.dp,
                            borderWidth = 2.dp,
                            cornerRadius = 0.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = memory,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.SansSerif,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                // Simple Cross icon for deleting
                                Text(
                                    text = "✕",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .clickable { onDeleteMemory(index) }
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer subtext in monospace italic dark blue
            Text(
                text = "Ray AI learns from your conversations to personalize its responses.",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B4F72),
                lineHeight = 15.sp
            )
        }
    }
}
