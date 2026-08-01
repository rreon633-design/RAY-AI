package com.example.ui.screens.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SystemPromptEditor(
    systemPrompt: String,
    onPromptChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val presets = listOf(
        Pair("General Assistant", "You are an intelligent, helpful offline AI assistant running locally on the user's mobile device CPU. Be clear, concise, accurate, and direct."),
        Pair("Coding Specialist", "You are an expert Kotlin, Python, and C++ software developer. Provide clean, well-documented, type-safe code snippets."),
        Pair("Math & Logic", "You are a precise offline mathematics and logic engine. Show step-by-step reasoning for equations and analytical queries."),
        Pair("Concise Summary", "Provide ultra-short, direct 2-3 sentence answers with zero fluff or conversational intros.")
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, ImmersiveBorder, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "System Prompt & Persona",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Guide how the offline CPU model behaves for all new messages",
                fontSize = 12.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Preset System Persona:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                items(presets) { (title, prompt) ->
                    SuggestionChip(
                        onClick = { onPromptChanged(prompt) },
                        label = { Text(title, fontSize = 11.sp, color = TextSecondary) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = ImmersiveSurface
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = ImmersiveBorder
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = systemPrompt,
                onValueChange = onPromptChanged,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentPurple,
                    unfocusedBorderColor = ImmersiveBorder,
                    focusedContainerColor = ImmersiveSurface,
                    unfocusedContainerColor = ImmersiveSurface,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
        }
    }
}

