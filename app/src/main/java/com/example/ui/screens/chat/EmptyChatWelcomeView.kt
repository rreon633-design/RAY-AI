package com.example.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AiModelInfo
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class StarterPrompt(
    val title: String,
    val subtitle: String,
    val prompt: String,
    val icon: ImageVector
)

@Composable
fun EmptyChatWelcomeView(
    activeModel: AiModelInfo,
    onSelectPrompt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val starters = listOf(
        StarterPrompt(
            title = "Build Web Component",
            subtitle = "Render live HTML, CSS & JS artifacts",
            prompt = "Create a responsive HTML/CSS pricing card with interactive hover effects and modern layout",
            icon = Icons.Default.Web
        ),
        StarterPrompt(
            title = "Help Me Code",
            subtitle = "Algorithms, debugging & Kotlin snippets",
            prompt = "Write a high-performance Kotlin binary search function with detailed comments",
            icon = Icons.Default.Code
        ),
        StarterPrompt(
            title = "Brainstorm Ideas",
            subtitle = "Get creative concepts for your project",
            prompt = "Brainstorm 5 innovative feature ideas for an AI app with local offline privacy",
            icon = Icons.Default.AutoAwesome
        ),
        StarterPrompt(
            title = "Summarize & Write",
            subtitle = "Draft essays, emails, or clear summaries",
            prompt = "Explain how transformer self-attention mechanisms work in simple terms",
            icon = Icons.Default.Edit
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // RAY AI Sunrise Sun logo icon
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(PurpleContainer, shape = CircleShape)
                .border(1.dp, AccentPurple.copy(alpha = 0.3f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = "RAY AI Engine",
                tint = AccentPurple,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "RAY AI",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "What can I create for you today?",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Powered by ${activeModel.name} • 100% On-Device",
            fontSize = 12.sp,
            color = TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Grid of ChatGPT/Claude style starter cards
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            starters.forEach { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ImmersiveSurfaceVariant, shape = RoundedCornerShape(16.dp))
                        .border(1.dp, ImmersiveBorderSubtle, shape = RoundedCornerShape(16.dp))
                        .clickable { onSelectPrompt(item.prompt) }
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(ImmersiveSurface, shape = RoundedCornerShape(10.dp))
                                .border(1.dp, ImmersiveBorder, shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = AccentPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = item.subtitle,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

