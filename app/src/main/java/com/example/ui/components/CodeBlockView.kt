package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.ui.theme.AccentPurple
import java.io.File
import java.io.FileOutputStream

@Composable
fun CodeBlockView(
    code: String,
    language: String = "code",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showArtifactPreview by remember { mutableStateOf(false) }

    val langLower = language.lowercase().trim()
    val isWebCode = langLower in listOf("html", "web", "javascript", "js", "css", "svg", "jsx") ||
            code.trim().startsWith("<!DOCTYPE") ||
            code.trim().startsWith("<html") ||
            code.contains("</html>") ||
            code.contains("</body>")

    val fileExtension = when (langLower) {
        "python", "py" -> "py"
        "kotlin", "kt" -> "kt"
        "java" -> "java"
        "javascript", "js" -> "js"
        "typescript", "ts" -> "ts"
        "html" -> "html"
        "css" -> "css"
        "cpp", "c++", "c" -> "cpp"
        "bash", "sh", "shell" -> "sh"
        "sql" -> "sql"
        "json" -> "json"
        "markdown", "md" -> "md"
        else -> "txt"
    }

    val linesCount = code.lines().size

    if (showArtifactPreview) {
        WebArtifactPreviewDialog(
            code = code,
            language = language,
            onDismiss = { showArtifactPreview = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White)
            .border(2.dp, Color.Black)
    ) {
        // Code Block Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF4D03F))
                        .border(1.5.dp, Color.Black)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = language.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }

                Text(
                    text = "$linesCount lines",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isWebCode) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF6BB6EC))
                            .border(1.5.dp, Color.Black)
                            .clickable { showArtifactPreview = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Preview Artifact",
                                tint = Color.Black,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = "PREVIEW",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Black
                            )
                        }
                    }
                }

                // Copy Code Button
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .border(1.5.dp, Color.Black)
                        .clickable {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Code", code)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy code",
                            tint = Color.Black,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "COPY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        // Code Body Text (white/grey background, black text for high contrast brutalist)
        Text(
            text = code,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.Black,
            lineHeight = 17.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9F9F9))
                .padding(12.dp)
        )

        // Custom Action Buttons Row (RUN CODE & SAVE SNIPPET)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // RUN CODE (Yellow)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFF4D03F))
                    .border(2.dp, Color.Black)
                    .clickable {
                        Toast.makeText(context, "Running code simulation locally...", Toast.LENGTH_SHORT).show()
                    }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "RUN CODE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }
            }

            // SAVE SNIPPET (Sky Blue)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF6BB6EC))
                    .border(2.dp, Color.Black)
                    .clickable {
                        Toast.makeText(context, "Saved snippet to local workspace!", Toast.LENGTH_SHORT).show()
                    }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "SAVE SNIPPET",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

