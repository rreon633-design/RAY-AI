package com.example.ui.components

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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import java.io.File
import java.io.FileOutputStream

@Composable
fun MarkdownTextView(
    text: String,
    textColor: Color = TextPrimary,
    isUser: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isDocumentWriting = !isUser && (
            text.startsWith("#") ||
            text.startsWith("Dear") ||
            text.startsWith("Subject:") ||
            text.contains("\n#") ||
            text.length > 300
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Document Header Bar for AI Writings & Long Text / Markdown Articles / Letters
        if (isDocumentWriting) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ImmersiveSurface, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, ImmersiveBorderSubtle, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Document Writing",
                        tint = AccentPurple,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Written Content & Document",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentPurple
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Download Document (.md / .txt) Button
                    IconButton(
                        onClick = {
                            try {
                                val fileName = "document_${System.currentTimeMillis()}.md"
                                val dir = context.getExternalFilesDir(null) ?: context.filesDir
                                val file = File(dir, fileName)
                                FileOutputStream(file).use { out ->
                                    out.write(text.toByteArray())
                                }
                                Toast.makeText(context, "Downloaded document: $fileName", Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Document Text", text))
                                Toast.makeText(context, "Copied document to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download Document",
                            tint = AccentPurple,
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    // Copy Full Document Text Button
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Document Content", text)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied full document!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Document",
                            tint = TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }

        if (text.contains("```")) {
            val parts = text.split("```")
            parts.forEachIndexed { index, part ->
                if (index % 2 == 1) {
                    // Code block
                    val lines = part.trim().split("\n", limit = 2)
                    val lang = if (lines.size > 1 && lines[0].length < 15 && !lines[0].contains(" ")) lines[0] else "code"
                    val code = if (lines.size > 1 && lines[0].length < 15 && !lines[0].contains(" ")) lines[1] else part.trim()
                    CodeBlockView(code = code, language = lang)
                } else if (part.isNotBlank()) {
                    MarkdownBlock(part = part, textColor = textColor)
                }
            }
        } else {
            MarkdownBlock(part = text, textColor = textColor)
        }
    }
}

@Composable
private fun MarkdownBlock(
    part: String,
    textColor: Color
) {
    val lines = part.trim('\n').split("\n")

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        lines.forEach { line ->
            val trimmedLine = line.trim()
            when {
                // Header 1
                trimmedLine.startsWith("# ") -> {
                    val headerText = trimmedLine.removePrefix("# ").trim()
                    Text(
                        text = parseMarkdownToAnnotatedString(headerText, textColor),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                // Header 2
                trimmedLine.startsWith("## ") -> {
                    val headerText = trimmedLine.removePrefix("## ").trim()
                    Text(
                        text = parseMarkdownToAnnotatedString(headerText, textColor),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(top = 6.dp, bottom = 3.dp)
                    )
                }
                // Header 3
                trimmedLine.startsWith("### ") -> {
                    val headerText = trimmedLine.removePrefix("### ").trim()
                    Text(
                        text = parseMarkdownToAnnotatedString(headerText, textColor),
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }
                // Bullet List Item (* , - , + )
                trimmedLine.startsWith("* ") || trimmedLine.startsWith("- ") || trimmedLine.startsWith("+ ") -> {
                    val bulletText = trimmedLine.substring(2).trim()
                    Row(
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentPurple,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = parseMarkdownToAnnotatedString(bulletText, textColor),
                            fontSize = 14.sp,
                            color = textColor,
                            lineHeight = 22.sp
                        )
                    }
                }
                // Numbered List Item (1. , 2. , etc.)
                trimmedLine.matches(Regex("^\\d+\\.\\s+.*")) -> {
                    val dotIdx = trimmedLine.indexOf('.')
                    val numPrefix = trimmedLine.substring(0, dotIdx + 1)
                    val numText = trimmedLine.substring(dotIdx + 1).trim()
                    Row(
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = numPrefix,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentPurple,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = parseMarkdownToAnnotatedString(numText, textColor),
                            fontSize = 14.sp,
                            color = textColor,
                            lineHeight = 22.sp
                        )
                    }
                }
                // Blockquote (> )
                trimmedLine.startsWith("> ") -> {
                    val quoteText = trimmedLine.removePrefix("> ").trim()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color(0xFFF8FAFC), shape = RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFE2E8F0), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(20.dp)
                                .background(AccentPurple, shape = RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = parseMarkdownToAnnotatedString(quoteText, Color(0xFF334155)),
                            fontSize = 13.5.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF334155),
                            lineHeight = 20.sp
                        )
                    }
                }
                // Empty line
                trimmedLine.isEmpty() -> {
                    Spacer(modifier = Modifier.height(2.dp))
                }
                // Regular Paragraph
                else -> {
                    Text(
                        text = parseMarkdownToAnnotatedString(trimmedLine, textColor),
                        fontSize = 14.sp,
                        color = textColor,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

fun parseMarkdownToAnnotatedString(
    text: String,
    textColor: Color
): AnnotatedString {
    return buildAnnotatedString {
        var i = 0
        val len = text.length
        while (i < len) {
            when {
                // Inline Code (`...`)
                text[i] == '`' && (i + 1 < len && text[i + 1] != '`') -> {
                    val nextBacktick = text.indexOf('`', i + 1)
                    if (nextBacktick != -1) {
                        val codeContent = text.substring(i + 1, nextBacktick)
                        withStyle(
                            SpanStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.5.sp,
                                background = Color(0xFFE2E8F0),
                                color = Color(0xFF0F172A)
                            )
                        ) {
                            append(" $codeContent ")
                        }
                        i = nextBacktick + 1
                    } else {
                        append(text[i])
                        i++
                    }
                }
                // Bold (**...** or __...__)
                (text.startsWith("**", i) || text.startsWith("__", i)) -> {
                    val delim = text.substring(i, i + 2)
                    val nextDelim = text.indexOf(delim, i + 2)
                    if (nextDelim != -1) {
                        val boldContent = text.substring(i + 2, nextDelim)
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(boldContent)
                        }
                        i = nextDelim + 2
                    } else {
                        append(text[i])
                        i++
                    }
                }
                // Italic (*...* or _..._)
                (text[i] == '*' || text[i] == '_') && (i + 1 < len && text[i + 1] != text[i]) -> {
                    val delim = text[i].toString()
                    val nextDelim = text.indexOf(delim, i + 1)
                    if (nextDelim != -1) {
                        val italicContent = text.substring(i + 1, nextDelim)
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(italicContent)
                        }
                        i = nextDelim + 1
                    } else {
                        append(text[i])
                        i++
                    }
                }
                else -> {
                    append(text[i])
                    i++
                }
            }
        }
    }
}
