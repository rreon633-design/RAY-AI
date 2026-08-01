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
            .background(Color(0xFF0F172A), shape = RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF334155), shape = RoundedCornerShape(12.dp))
    ) {
        // Code Block Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E293B), shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = Color(0xFF334155),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = language.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "$linesCount lines",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isWebCode) {
                    Surface(
                        onClick = { showArtifactPreview = true },
                        color = AccentPurple,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Preview Artifact",
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Preview",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Download / Save Script Button
                IconButton(
                    onClick = {
                        try {
                            val fileName = "script_${System.currentTimeMillis()}.$fileExtension"
                            val dir = context.getExternalFilesDir(null) ?: context.filesDir
                            val file = File(dir, fileName)
                            FileOutputStream(file).use { out ->
                                out.write(code.toByteArray())
                            }
                            Toast.makeText(context, "Downloaded & saved script: $fileName", Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Script Code", code))
                            Toast.makeText(context, "Script copied to clipboard as file backup!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download script",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(15.dp)
                    )
                }

                // Copy Code Button
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Code", code)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy code",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        // Code Body Text
        Text(
            text = code,
            fontSize = 12.5.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFFF8FAFC),
            lineHeight = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
    }
}

