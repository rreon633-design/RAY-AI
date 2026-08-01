package com.example.ui.screens.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MessageEntity
import com.example.ui.components.CodeBlockView
import com.example.ui.components.MarkdownTextView
import com.example.ui.components.MetricBadge
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.OnAccentPurple
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.DocumentExporter

@Composable
fun ChatMessageItem(
    message: MessageEntity,
    onEditPrompt: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isUser = message.sender == "user"
    val context = LocalContext.current
    var isLiked by remember { mutableStateOf<Boolean?>(null) }
    var showExportMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(PurpleContainer, shape = CircleShape)
                    .border(1.dp, AccentPurple.copy(alpha = 0.3f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = "ChatGPT AI Engine",
                    tint = AccentPurple,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Surface(
                color = if (isUser) AccentPurple else ImmersiveSurfaceVariant,
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = if (isUser) 18.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 18.dp
                ),
                border = if (isUser) null else androidx.compose.foundation.BorderStroke(1.dp, ImmersiveBorderSubtle)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    MarkdownTextView(
                        text = message.text,
                        textColor = if (isUser) OnAccentPurple else TextPrimary,
                        isUser = isUser
                    )

                    if (isUser) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                // Copy User Prompt Button
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("User Prompt", message.text)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Prompt copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy Prompt",
                                        tint = OnAccentPurple.copy(alpha = 0.85f),
                                        modifier = Modifier.size(13.dp)
                                    )
                                }

                                // Modify / Edit Prompt Button
                                if (onEditPrompt != null) {
                                    IconButton(
                                        onClick = {
                                            onEditPrompt(message.text)
                                            Toast.makeText(context, "Loaded prompt into input bar to modify!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Modify Prompt",
                                            tint = OnAccentPurple.copy(alpha = 0.85f),
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (!isUser) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // ChatGPT style message action buttons (Copy, Export, Thumbs Up, Thumbs Down)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Export Dropdown Menu Button
                                Box {
                                    IconButton(
                                        onClick = { showExportMenu = true },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Download,
                                            contentDescription = "Export Document",
                                            tint = AccentPurple,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showExportMenu,
                                        onDismissRequest = { showExportMenu = false },
                                        modifier = Modifier.background(ImmersiveSurface)
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Export as .PDF", color = TextPrimary, fontSize = 13.sp) },
                                            leadingIcon = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = AccentPurple, modifier = Modifier.size(16.dp)) },
                                            onClick = {
                                                showExportMenu = false
                                                val res = DocumentExporter.exportToPdf(context, "AI_Document", message.text)
                                                if (res.isSuccess) {
                                                    Toast.makeText(context, "Exported PDF to Documents: ${res.getOrNull()?.name}", Toast.LENGTH_LONG).show()
                                                } else {
                                                    Toast.makeText(context, "PDF export error: ${res.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Export as .WORD (.docx)", color = TextPrimary, fontSize = 13.sp) },
                                            leadingIcon = { Icon(Icons.Default.TextSnippet, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp)) },
                                            onClick = {
                                                showExportMenu = false
                                                val res = DocumentExporter.exportToWordDocx(context, "AI_Document", message.text)
                                                if (res.isSuccess) {
                                                    Toast.makeText(context, "Exported Word docx to Documents: ${res.getOrNull()?.name}", Toast.LENGTH_LONG).show()
                                                } else {
                                                    Toast.makeText(context, "Word export error: ${res.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Export as .HTML", color = TextPrimary, fontSize = 13.sp) },
                                            leadingIcon = { Icon(Icons.Default.Download, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp)) },
                                            onClick = {
                                                showExportMenu = false
                                                val res = DocumentExporter.exportToHtml(context, "AI_Document", message.text)
                                                if (res.isSuccess) {
                                                    Toast.makeText(context, "Exported HTML to Documents: ${res.getOrNull()?.name}", Toast.LENGTH_LONG).show()
                                                } else {
                                                    Toast.makeText(context, "HTML export error: ${res.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Export as .MD / .TXT", color = TextPrimary, fontSize = 13.sp) },
                                            leadingIcon = { Icon(Icons.Default.TextSnippet, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp)) },
                                            onClick = {
                                                showExportMenu = false
                                                val res = DocumentExporter.exportToText(context, "AI_Document", message.text)
                                                if (res.isSuccess) {
                                                    Toast.makeText(context, "Exported Markdown file: ${res.getOrNull()?.name}", Toast.LENGTH_LONG).show()
                                                } else {
                                                    Toast.makeText(context, "Text export error: ${res.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("RAY AI Message", message.text)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                        contentDescription = "Copy message",
                                        tint = TextMuted,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { isLiked = if (isLiked == true) null else true },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isLiked == true) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                                        contentDescription = "Good response",
                                        tint = if (isLiked == true) AccentPurple else TextMuted,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { isLiked = if (isLiked == false) null else false },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isLiked == false) Icons.Default.ThumbDown else Icons.Outlined.ThumbDown,
                                        contentDescription = "Bad response",
                                        tint = if (isLiked == false) Color.Red else TextMuted,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(ImmersiveBorder, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

