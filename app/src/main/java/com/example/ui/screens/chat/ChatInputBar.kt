package com.example.ui.screens.chat

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveBottomBar
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.OnAccentPurple
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ChatInputBar(
    isGenerating: Boolean,
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onStopGeneration: () -> Unit,
    modifier: Modifier = Modifier
) {
    var attachedFileName by remember { mutableStateOf<String?>(null) }
    var showAttachmentMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val promptSuggestions = listOf(
        "Generate a responsive HTML/CSS pricing card",
        "Explain Quantum Computing in 2 sentences",
        "Write Kotlin binary search algorithm",
        "Draft a web app landing page template"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ImmersiveBottomBar)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        if (!isGenerating && inputText.isEmpty() && attachedFileName == null) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(promptSuggestions) { suggestion ->
                    Box(
                        modifier = Modifier
                            .background(ImmersiveSurfaceVariant, shape = RoundedCornerShape(16.dp))
                            .border(1.dp, ImmersiveBorderSubtle, shape = RoundedCornerShape(16.dp))
                            .clickable { onSendMessage(suggestion) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = suggestion,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Attached File Banner Chip
        AnimatedVisibility(visible = attachedFileName != null) {
            attachedFileName?.let { fileName ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .background(PurpleContainer, shape = RoundedCornerShape(12.dp))
                        .border(1.dp, AccentPurple.copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = AccentPurple,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Attached: $fileName",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentPurple
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { attachedFileName = null },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove file",
                            tint = AccentPurple,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Attachment Dropdown Menu
        if (showAttachmentMenu) {
            DropdownMenu(
                expanded = showAttachmentMenu,
                onDismissRequest = { showAttachmentMenu = false },
                modifier = Modifier.background(ImmersiveSurface)
            ) {
                DropdownMenuItem(
                    text = { Text("Code File (.kt, .js, .html)", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, tint = AccentPurple) },
                    onClick = {
                        attachedFileName = "sample_code.html"
                        showAttachmentMenu = false
                        Toast.makeText(context, "Attached sample HTML file", Toast.LENGTH_SHORT).show()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Text Document (.txt, .md)", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = AccentPurple) },
                    onClick = {
                        attachedFileName = "notes.md"
                        showAttachmentMenu = false
                        Toast.makeText(context, "Attached markdown file", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // Input Box Container
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(ImmersiveSurfaceVariant, shape = RoundedCornerShape(28.dp))
                .border(1.dp, ImmersiveBorder, shape = RoundedCornerShape(28.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            // Plus Add Attachment Button
            IconButton(
                onClick = { showAttachmentMenu = true },
                modifier = Modifier
                    .size(40.dp)
                    .background(ImmersiveSurface, shape = CircleShape)
                    .border(1.dp, ImmersiveBorderSubtle, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Files",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Main Text Field
            TextField(
                value = inputText,
                onValueChange = onInputTextChange,
                placeholder = { Text("Ask RAY AI anything...", fontSize = 14.sp, color = TextMuted) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                maxLines = 5
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Action Button (Send / Stop Animated Switch)
            AnimatedContent(
                targetState = isGenerating,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "SendStopAnimation"
            ) { generating ->
                if (generating) {
                    IconButton(
                        onClick = onStopGeneration,
                        modifier = Modifier
                            .size(42.dp)
                            .background(ErrorRed, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop Generation",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    val canSend = inputText.isNotBlank() || attachedFileName != null
                    IconButton(
                        onClick = {
                            if (canSend) {
                                val fullMessage = if (attachedFileName != null) {
                                    "[Attached File: $attachedFileName]\n\n$inputText".trim()
                                } else {
                                    inputText
                                }
                                onSendMessage(fullMessage)
                                onInputTextChange("")
                                attachedFileName = null
                            }
                        },
                        enabled = canSend,
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                if (canSend) AccentPurple else ImmersiveBorder,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Message",
                            tint = if (canSend) OnAccentPurple else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}


