package com.example.ui.screens.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MessageEntity
import com.example.ui.components.MarkdownTextView
import com.example.ui.components.NeobrutalCard
import com.example.ui.components.NeobrutalIllustration
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

    // Mock static timestamp for demo fidelity
    val displayTime = if (isUser) "10:25 AM" else "10:24 AM"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Tag Headers
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            if (isUser) {
                // Timestamp left, Tag right
                Text(
                    text = displayTime,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0xFF0D7BB3)) // Deep Sky Blue tag
                        .border(2.dp, Color.Black)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "YOU",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            } else {
                // Tag left, Timestamp right
                Box(
                    modifier = Modifier
                        .background(Color.Black)
                        .border(2.dp, Color.Black)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "RAY AI",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = displayTime,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }

        // Main Message Card
        NeobrutalCard(
            modifier = Modifier.widthIn(max = 330.dp),
            backgroundColor = if (isUser) Color(0xFF6BB6EC) else Color.White,
            borderColor = Color.Black,
            borderWidth = 2.dp,
            shadowOffset = 4.dp
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Markdown text view with custom content text color
                MarkdownTextView(
                    text = message.text,
                    textColor = Color.Black,
                    isUser = isUser
                )

                // Render customized illustration study if requested
                if (!isUser && message.text.contains("structural study")) {
                    Spacer(modifier = Modifier.height(12.dp))
                    NeobrutalIllustration()
                }

                // Tokens details / metrics row (if any)
                if (!isUser && message.tokensPerSecond > 0f) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF4D03F))
                            .border(1.5.dp, Color.Black)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${message.cpuThreads ?: 4} CORES | ${String.format("%.1f", message.tokensPerSecond)} t/s",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action Buttons Toolbar inside message
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isUser) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Copy Prompt
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("User Prompt", message.text)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Prompt copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Prompt",
                                    tint = Color.Black,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Edit Prompt
                            if (onEditPrompt != null) {
                                IconButton(
                                    onClick = {
                                        onEditPrompt(message.text)
                                        Toast.makeText(context, "Loaded to editor!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Modify Prompt",
                                        tint = Color.Black,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        // AI message action buttons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Export Menu
                            Box {
                                IconButton(
                                    onClick = { showExportMenu = true },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = "Export Document",
                                        tint = Color.Black,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showExportMenu,
                                    onDismissRequest = { showExportMenu = false },
                                    modifier = Modifier
                                        .background(Color.White)
                                        .border(2.dp, Color.Black)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Export as .PDF", color = Color.Black, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                                        leadingIcon = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp)) },
                                        onClick = {
                                            showExportMenu = false
                                            val res = DocumentExporter.exportToPdf(context, "AI_Document", message.text)
                                            if (res.isSuccess) {
                                                Toast.makeText(context, "Exported PDF to Documents", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Export as .MD / .TXT", color = Color.Black, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                                        leadingIcon = { Icon(Icons.Default.TextSnippet, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp)) },
                                        onClick = {
                                            showExportMenu = false
                                            val res = DocumentExporter.exportToText(context, "AI_Document", message.text)
                                            if (res.isSuccess) {
                                                Toast.makeText(context, "Exported Markdown file", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                }
                            }

                            // Copy message
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("RAY AI Message", message.text)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy message",
                                    tint = Color.Black,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Thumbs up
                            IconButton(
                                onClick = { isLiked = if (isLiked == true) null else true },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (isLiked == true) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                                    contentDescription = "Good response",
                                    tint = if (isLiked == true) Color(0xFF0D7BB3) else Color.Black,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Thumbs down
                            IconButton(
                                onClick = { isLiked = if (isLiked == false) null else false },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (isLiked == false) Icons.Default.ThumbDown else Icons.Outlined.ThumbDown,
                                    contentDescription = "Bad response",
                                    tint = if (isLiked == false) Color.Red else Color.Black,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
