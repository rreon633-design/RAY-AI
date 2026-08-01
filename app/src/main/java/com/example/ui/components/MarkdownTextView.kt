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
import androidx.compose.runtime.remember
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

sealed class RenderElement {
    data class Header(val level: Int, val text: String) : RenderElement()
    data class Bullet(val bulletText: String, val isBoldHeader: Boolean, val title: String = "", val desc: String = "") : RenderElement()
    data class Numbered(val prefix: String, val text: String) : RenderElement()
    data class Quote(val text: String) : RenderElement()
    data class Progress(val label: String, val percentage: Int) : RenderElement()
    data class Table(val headers: List<String>, val rows: List<List<String>>) : RenderElement()
    data class Chart(val title: String, val bars: List<Pair<String, Double>>) : RenderElement()
    data class StepCard(val stepNum: String, val title: String, val desc: String) : RenderElement()
    data class TipCard(val title: String, val desc: String) : RenderElement()
    data class ImageResult(val prompt: String) : RenderElement()
    data class Paragraph(val text: String) : RenderElement()
    object SpacerElement : RenderElement()
}

fun parsePartToElements(part: String): List<RenderElement> {
    val lines = part.trim('\n').split("\n")
    val elements = mutableListOf<RenderElement>()
    var i = 0
    val total = lines.size
    
    while (i < total) {
        val line = lines[i]
        val trimmedLine = line.trim()
        
        when {
            trimmedLine.isEmpty() -> {
                elements.add(RenderElement.SpacerElement)
                i++
            }
            
            trimmedLine.startsWith("|") && trimmedLine.endsWith("|") -> {
                val tableLines = mutableListOf<String>()
                while (i < total && lines[i].trim().startsWith("|") && lines[i].trim().endsWith("|")) {
                    tableLines.add(lines[i].trim())
                    i++
                }
                
                if (tableLines.size >= 1) {
                    val rawHeaders = tableLines[0].split("|").map { it.trim() }.filter { it.isNotEmpty() }
                    val dataStartIndex = if (tableLines.size > 1 && tableLines[1].contains("---")) 2 else 1
                    
                    val rows = mutableListOf<List<String>>()
                    for (k in dataStartIndex until tableLines.size) {
                        val rowCells = tableLines[k].split("|").map { it.trim() }.filterIndexed { idx, _ -> idx > 0 && idx < tableLines[k].split("|").size - 1 }
                        rows.add(rowCells)
                    }
                    
                    elements.add(RenderElement.Table(rawHeaders, rows))
                }
            }
            
            trimmedLine.startsWith("[chart:") && trimmedLine.endsWith("]") -> {
                val content = trimmedLine.removePrefix("[chart:").removeSuffix("]").trim()
                val parts = content.split("|")
                val title = if (parts.isNotEmpty()) parts[0].trim() else "Chart"
                val bars = mutableListOf<Pair<String, Double>>()
                for (k in 1 until parts.size) {
                    val barPart = parts[k].trim()
                    val eqIdx = barPart.indexOf('=')
                    if (eqIdx != -1) {
                        val label = barPart.substring(0, eqIdx).trim()
                        val valStr = barPart.substring(eqIdx + 1).trim()
                        val value = valStr.toDoubleOrNull() ?: 0.0
                        bars.add(Pair(label, value))
                    }
                }
                elements.add(RenderElement.Chart(title, bars))
                i++
            }
            
            (trimmedLine.startsWith("[visual:") || trimmedLine.startsWith("[image:")) && trimmedLine.endsWith("]") -> {
                val prefix = if (trimmedLine.startsWith("[visual:")) "[visual:" else "[image:"
                val prompt = trimmedLine.removePrefix(prefix).removeSuffix("]").trim()
                elements.add(RenderElement.ImageResult(prompt))
                i++
            }
            
            (trimmedLine.startsWith("[tip:") || trimmedLine.startsWith("[pro-tip:")) && trimmedLine.endsWith("]") -> {
                val prefix = if (trimmedLine.startsWith("[tip:")) "[tip:" else "[pro-tip:"
                val content = trimmedLine.removePrefix(prefix).removeSuffix("]").trim()
                val parts = content.split("|")
                val title = if (parts.isNotEmpty()) parts[0].trim() else "PRO TIP"
                val desc = if (parts.size > 1) parts[1].trim() else ""
                elements.add(RenderElement.TipCard(title, desc))
                i++
            }
            
            trimmedLine.startsWith("[step:") && trimmedLine.endsWith("]") -> {
                val content = trimmedLine.removePrefix("[step:").removeSuffix("]").trim()
                val parts = content.split("|")
                val stepNum = if (parts.isNotEmpty()) parts[0].trim() else "01"
                val title = if (parts.size > 1) parts[1].trim() else ""
                val desc = if (parts.size > 2) parts[2].trim() else ""
                elements.add(RenderElement.StepCard(stepNum, title, desc))
                i++
            }
            
            trimmedLine.startsWith("[progress:") && trimmedLine.endsWith("]") -> {
                val content = trimmedLine.removePrefix("[progress:").removeSuffix("]").trim()
                val parts = content.split("|")
                val label = if (parts.size > 1) parts[0].trim() else "PROGRESS"
                val pctStr = if (parts.size > 1) parts[1].trim() else parts[0].trim()
                val pct = pctStr.removeSuffix("%").trim().toIntOrNull() ?: 50
                elements.add(RenderElement.Progress(label, pct))
                i++
            }
            
            trimmedLine.startsWith("# ") -> {
                elements.add(RenderElement.Header(1, trimmedLine.removePrefix("# ").trim()))
                i++
            }
            trimmedLine.startsWith("## ") -> {
                elements.add(RenderElement.Header(2, trimmedLine.removePrefix("## ").trim()))
                i++
            }
            trimmedLine.startsWith("### ") -> {
                elements.add(RenderElement.Header(3, trimmedLine.removePrefix("### ").trim()))
                i++
            }
            
            trimmedLine.startsWith("* ") || trimmedLine.startsWith("- ") || trimmedLine.startsWith("+ ") -> {
                val bulletText = trimmedLine.substring(2).trim()
                val isBoldHeader = bulletText.startsWith("**") && (bulletText.contains("**: ") || bulletText.contains("** - ") || bulletText.contains("** – "))
                if (isBoldHeader) {
                    val endBoldIdx = bulletText.indexOf("**", 2)
                    if (endBoldIdx != -1) {
                        val title = bulletText.substring(2, endBoldIdx).trim()
                        var desc = bulletText.substring(endBoldIdx + 2).trim()
                        if (desc.startsWith(":") || desc.startsWith("-") || desc.startsWith("–")) {
                            desc = desc.substring(1).trim()
                        }
                        elements.add(RenderElement.Bullet(bulletText = bulletText, isBoldHeader = true, title = title, desc = desc))
                    } else {
                        elements.add(RenderElement.Bullet(bulletText = bulletText, isBoldHeader = false))
                    }
                } else {
                    elements.add(RenderElement.Bullet(bulletText = bulletText, isBoldHeader = false))
                }
                i++
            }
            
            trimmedLine.matches(Regex("^\\d+\\.\\s+.*")) -> {
                val dotIdx = trimmedLine.indexOf('.')
                val numPrefix = trimmedLine.substring(0, dotIdx + 1)
                val numText = trimmedLine.substring(dotIdx + 1).trim()
                
                val isBoldStep = numText.startsWith("**") && numText.contains("**") && (numText.contains("**: ") || numText.contains("** - "))
                if (isBoldStep) {
                    val endBoldIdx = numText.indexOf("**", 2)
                    if (endBoldIdx != -1) {
                        val title = numText.substring(2, endBoldIdx).trim()
                        var desc = numText.substring(endBoldIdx + 2).trim()
                        if (desc.startsWith(":") || desc.startsWith("-")) {
                            desc = desc.substring(1).trim()
                        }
                        val stepNum = if (numPrefix.removeSuffix(".").length == 1) "0${numPrefix.removeSuffix(".")}" else numPrefix.removeSuffix(".")
                        elements.add(RenderElement.StepCard(stepNum, title, desc))
                    } else {
                        elements.add(RenderElement.Numbered(numPrefix, numText))
                    }
                } else {
                    elements.add(RenderElement.Numbered(numPrefix, numText))
                }
                i++
            }
            
            trimmedLine.startsWith("> ") -> {
                val quoteText = trimmedLine.removePrefix("> ").trim()
                if (quoteText.startsWith("PRO TIP:") || quoteText.startsWith("**PRO TIP:**")) {
                    val content = quoteText.removePrefix("**PRO TIP:**").removePrefix("PRO TIP:").trim()
                    val parts = content.split(":")
                    val title = if (parts.isNotEmpty()) parts[0].trim() else "PRO TIP"
                    val desc = if (parts.size > 1) content.substring(parts[0].length + 1).trim() else content
                    elements.add(RenderElement.TipCard(title, desc))
                } else {
                    elements.add(RenderElement.Quote(quoteText))
                }
                i++
            }
            
            else -> {
                elements.add(RenderElement.Paragraph(trimmedLine))
                i++
            }
        }
    }
    
    return elements
}

@Composable
private fun MarkdownBlock(
    part: String,
    textColor: Color
) {
    val elements = remember(part) { parsePartToElements(part) }
    
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        for (element in elements) {
            when (element) {
                is RenderElement.SpacerElement -> {
                    Spacer(modifier = Modifier.height(2.dp))
                }
                is RenderElement.Header -> {
                    val fontSize = when (element.level) {
                        1 -> 18.sp
                        2 -> 16.sp
                        else -> 14.5.sp
                    }
                    Text(
                        text = parseMarkdownToAnnotatedString(element.text, textColor),
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        lineHeight = (fontSize.value + 6).sp,
                        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                    )
                }
                is RenderElement.Bullet -> {
                    if (element.isBoldHeader) {
                        val cardBgColor = when (element.title.lowercase()) {
                            "contrast" -> Color(0xFFF4D03F)
                            "geometry" -> Color(0xFFF1948A)
                            else -> Color(0xFF85C1E9)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        NeobrutalCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = cardBgColor,
                            borderColor = Color.Black,
                            borderWidth = 1.5.dp,
                            shadowOffset = 3.dp
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = element.title.uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = element.desc,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Black
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    } else {
                        Row(
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "•",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = parseMarkdownToAnnotatedString(element.bulletText, textColor),
                                fontSize = 13.sp,
                                color = textColor,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
                is RenderElement.Numbered -> {
                    Row(
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = element.prefix,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentPurple,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = parseMarkdownToAnnotatedString(element.text, textColor),
                            fontSize = 13.sp,
                            color = textColor,
                            lineHeight = 19.sp
                        )
                    }
                }
                is RenderElement.Quote -> {
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
                            text = parseMarkdownToAnnotatedString(element.text, Color(0xFF334155)),
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF334155),
                            lineHeight = 19.sp
                        )
                    }
                }
                is RenderElement.Progress -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${element.label.uppercase()} ${element.percentage}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black
                        )
                        NeobrutalProgressBar(
                            progress = element.percentage / 100f,
                            progressColor = Color(0xFFF4D03F),
                            barHeight = 14.dp
                        )
                    }
                }
                is RenderElement.Table -> {
                    NeobrutalTable(
                        headers = element.headers,
                        rows = element.rows,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                is RenderElement.Chart -> {
                    NeobrutalChart(
                        title = element.title,
                        bars = element.bars,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                is RenderElement.StepCard -> {
                    NeobrutalStepCard(
                        stepNumber = element.stepNum,
                        title = element.title,
                        description = element.desc,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                is RenderElement.TipCard -> {
                    NeobrutalTipCard(
                        title = element.title,
                        description = element.desc,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                is RenderElement.ImageResult -> {
                    NeobrutalImageResult(
                        prompt = element.prompt,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                is RenderElement.Paragraph -> {
                    Text(
                        text = parseMarkdownToAnnotatedString(element.text, textColor),
                        fontSize = 13.sp,
                        color = textColor,
                        lineHeight = 19.sp
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
