package com.example.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChatEntity
import com.example.ui.components.NeobrutalButton
import com.example.ui.components.NeobrutalCard

@Composable
fun ChatDrawerSheet(
    sessions: List<ChatEntity>,
    currentSessionId: Long?,
    onSelectSession: (Long) -> Unit,
    onNewChat: () -> Unit,
    onDeleteSession: (Long) -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToModels: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier.width(300.dp),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp)
        ) {
            // --- HEADER: Logo and Title ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .border(2.dp, Color.Black)
                        .background(Color(0xFFF4D03F)) // Bright yellow background
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "RAY AI Logo",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = "RAY AI",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "LOCAL INTELLIGENCE",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- "Start New Chat" PRIMARY BUTTON ---
            NeobrutalButton(
                onClick = onNewChat,
                backgroundColor = Color(0xFFF4D03F), // Neobrutal Yellow
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NEW CHAT",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Solid black thick divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp)
                    .background(Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section Label
            Text(
                text = "RECENT CHATS",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // --- RECENT CHATS LIST ---
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(sessions, key = { it.id }) { session ->
                    val isSelected = session.id == currentSessionId
                    val itemBgColor = if (isSelected) Color(0xFFEBF5FB) else Color.White // Light Blue or White
                    val itemBorderWidth = if (isSelected) 2.dp else 1.5.dp
                    val itemShadowOffset = if (isSelected) 3.dp else 0.dp

                    NeobrutalCard(
                        backgroundColor = itemBgColor,
                        borderWidth = itemBorderWidth,
                        shadowOffset = itemShadowOffset,
                        cornerRadius = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectSession(session.id) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = session.title,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                        fontFamily = FontFamily.SansSerif,
                                        color = Color.Black,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = session.activeModelName.uppercase(),
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Delete session icon button (small square black border)
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .border(1.5.dp, Color.Black)
                                    .background(Color(0xFFFDEDEC)) // Light soft red
                                    .clickable { onDeleteSession(session.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete chat",
                                    tint = Color.Black,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Solid black thin divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- BOTTOM NAVIGATION NAVIGATION OPTIONS ---
            // 1. Models Hub Link Button
            NeobrutalButton(
                onClick = onNavigateToModels,
                backgroundColor = Color.White,
                borderWidth = 2.dp,
                shadowOffset = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DownloadForOffline,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "MODELS HUB",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Settings Link Button
            NeobrutalButton(
                onClick = onNavigateToSettings,
                backgroundColor = Color.White,
                borderWidth = 2.dp,
                shadowOffset = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "SETTINGS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
