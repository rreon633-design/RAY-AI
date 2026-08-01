package com.example.ui.screens.chat

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChatEntity
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.OnAccentPurple
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

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
        drawerContainerColor = ImmersiveSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(PurpleContainer, shape = RoundedCornerShape(10.dp))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "RAY AI",
                        tint = AccentPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "RAY AI",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Sunrise Minimal Intelligence",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNewChat,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple, contentColor = OnAccentPurple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start New Chat", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = ImmersiveBorderSubtle)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "RECENT CHATS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(sessions, key = { it.id }) { session ->
                    val isSelected = session.id == currentSessionId
                    val bgColor = if (isSelected) ImmersiveSurfaceVariant else Color.Transparent
                    val textColor = if (isSelected) AccentPurple else TextPrimary

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bgColor, shape = RoundedCornerShape(10.dp))
                            .clickable { onSelectSession(session.id) }
                            .padding(horizontal = 10.dp, vertical = 10.dp),
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
                                tint = textColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = session.title,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = textColor,
                                    maxLines = 1
                                )
                                Text(
                                    text = session.activeModelName,
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        IconButton(
                            onClick = { onDeleteSession(session.id) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = ImmersiveBorderSubtle)
            Spacer(modifier = Modifier.height(8.dp))

            // Navigation Options
            NavigationDrawerItem(
                label = { Text("Models Hub", fontWeight = FontWeight.Medium, fontSize = 13.sp) },
                icon = { Icon(Icons.Default.DownloadForOffline, contentDescription = null, tint = AccentPurple) },
                selected = false,
                onClick = onNavigateToModels,
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = ImmersiveSurfaceVariant,
                    unselectedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(vertical = 2.dp)
            )

            NavigationDrawerItem(
                label = { Text("Settings & Preferences", fontWeight = FontWeight.Medium, fontSize = 13.sp) },
                icon = { Icon(Icons.Default.Settings, contentDescription = null, tint = AccentPurple) },
                selected = false,
                onClick = onNavigateToSettings,
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = ImmersiveSurfaceVariant,
                    unselectedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}


