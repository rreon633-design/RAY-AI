package com.example.ui.screens.chat

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeobrutalButton
import com.example.ui.components.NeobrutalIconButton
import com.example.ui.components.NeobrutalTextField

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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Attached File Banner Chip (Neobrutal style)
        AnimatedVisibility(visible = attachedFileName != null) {
            attachedFileName?.let { fileName ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .background(Color(0xFFF4D03F)) // Neobrutal Yellow banner
                        .border(2.dp, Color.Black)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ATTACHED: $fileName",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { attachedFileName = null },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove file",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Attachment Dropdown Menu (Styled Neobrutal)
        if (showAttachmentMenu) {
            DropdownMenu(
                expanded = showAttachmentMenu,
                onDismissRequest = { showAttachmentMenu = false },
                modifier = Modifier
                    .background(Color.White)
                    .border(2.dp, Color.Black)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Code File (.kt, .js, .html)",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, tint = Color.Black) },
                    onClick = {
                        attachedFileName = "sample_code.html"
                        showAttachmentMenu = false
                        Toast.makeText(context, "Attached sample HTML file", Toast.LENGTH_SHORT).show()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Text Document (.txt, .md)",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = Color.Black) },
                    onClick = {
                        attachedFileName = "notes.md"
                        showAttachmentMenu = false
                        Toast.makeText(context, "Attached markdown file", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // Input Box Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Plus Add Attachment Button in Neobrutalist design
            NeobrutalIconButton(
                onClick = { showAttachmentMenu = true },
                icon = Icons.Default.Add,
                contentDescription = "Add Files",
                backgroundColor = Color.White,
                borderColor = Color.Black,
                borderWidth = 2.dp,
                shadowOffset = 3.dp,
                cornerRadius = 0.dp,
                modifier = Modifier.size(42.dp)
            )

            // Main Text Field in Neobrutalist design
            NeobrutalTextField(
                value = inputText,
                onValueChange = onInputTextChange,
                placeholder = "Explain more about typography...",
                backgroundColor = Color.White,
                borderColor = Color.Black,
                borderWidth = 2.dp,
                shadowOffset = 0.dp, // Flat as in mockup
                modifier = Modifier.weight(1f)
            )

            // SEND / STOP Button
            if (isGenerating) {
                // STOP button in Coral red
                NeobrutalButton(
                    onClick = onStopGeneration,
                    backgroundColor = Color(0xFFE25C4E),
                    borderColor = Color.Black,
                    borderWidth = 2.dp,
                    shadowOffset = 4.dp,
                    modifier = Modifier.height(42.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "STOP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                val canSend = inputText.isNotBlank() || attachedFileName != null
                NeobrutalButton(
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
                    backgroundColor = if (canSend) Color(0xFFF4D03F) else Color(0xFFDDDDDD),
                    borderColor = Color.Black,
                    borderWidth = 2.dp,
                    shadowOffset = 4.dp,
                    modifier = Modifier.height(42.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SEND",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
