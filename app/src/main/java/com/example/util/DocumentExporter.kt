package com.example.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.StaticLayout
import android.text.TextPaint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object DocumentExporter {

    fun exportToPdf(context: Context, title: String, content: String): Result<File> {
        return try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            val textPaint = TextPaint().apply {
                color = Color.BLACK
                textSize = 12f
                isAntiAlias = true
            }

            val titlePaint = TextPaint().apply {
                color = Color.rgb(79, 70, 229) // Indigo
                textSize = 18f
                isFakeBoldText = true
                isAntiAlias = true
            }

            canvas.drawText(title, 40f, 50f, titlePaint)

            val margin = 40
            val width = pageInfo.pageWidth - (margin * 2)

            val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(content, 0, content.length, textPaint, width)
                    .setLineSpacing(0f, 1.2f)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                StaticLayout(content, textPaint, width, android.text.Layout.Alignment.ALIGN_NORMAL, 1.2f, 0f, false)
            }

            canvas.save()
            canvas.translate(40f, 80f)
            staticLayout.draw(canvas)
            canvas.restore()

            pdfDocument.finishPage(page)

            val fileName = "${title.replace(Regex("[^a-zA-Z0-9_-]"), "_")}_${System.currentTimeMillis()}.pdf"
            val file = saveFileToStorage(context, fileName, "application/pdf") { outStream ->
                pdfDocument.writeTo(outStream)
            }

            pdfDocument.close()
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun exportToWordDocx(context: Context, title: String, content: String): Result<File> {
        return try {
            val formattedHtml = """
                <html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:w='urn:schemas-microsoft-com:office:word' xmlns='http://www.w3.org/TR/REC-html40'>
                <head>
                    <meta charset='utf-8'>
                    <title>$title</title>
                    <style>
                        body { font-family: 'Calibri', 'Arial', sans-serif; font-size: 11pt; line-height: 1.5; margin: 1in; color: #1e293b; }
                        h1 { color: #4338ca; font-size: 20pt; border-bottom: 2px solid #e2e8f0; padding-bottom: 8px; }
                        p { margin-bottom: 12px; }
                        pre { background: #f8fafc; padding: 10px; border: 1px solid #cbd5e1; font-family: 'Consolas', monospace; }
                    </style>
                </head>
                <body>
                    <h1>$title</h1>
                    <div>${content.replace("\n", "<br>")}</div>
                </body>
                </html>
            """.trimIndent()

            val fileName = "${title.replace(Regex("[^a-zA-Z0-9_-]"), "_")}_${System.currentTimeMillis()}.docx"
            val file = saveFileToStorage(context, fileName, "application/vnd.openxmlformats-officedocument.wordprocessingml.document") { outStream ->
                outStream.write(formattedHtml.toByteArray(Charsets.UTF_8))
            }
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun exportToHtml(context: Context, title: String, content: String): Result<File> {
        return try {
            val fullHtml = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>$title</title>
                    <style>
                        body { font-family: system-ui, -apple-system, sans-serif; background-color: #0f172a; color: #f8fafc; max-width: 800px; margin: 40px auto; padding: 24px; line-height: 1.6; }
                        h1 { color: #38bdf8; border-bottom: 1px solid #334155; padding-bottom: 12px; }
                        .content { background: #1e293b; padding: 20px; border-radius: 12px; border: 1px solid #334155; white-space: pre-wrap; }
                        footer { margin-top: 30px; font-size: 0.85rem; color: #94a3b8; text-align: center; }
                    </style>
                </head>
                <body>
                    <h1>$title</h1>
                    <div class="content">${content.replace("<", "&lt;").replace(">", "&gt;")}</div>
                    <footer>Exported from Offline Local AI</footer>
                </body>
                </html>
            """.trimIndent()

            val fileName = "${title.replace(Regex("[^a-zA-Z0-9_-]"), "_")}_${System.currentTimeMillis()}.html"
            val file = saveFileToStorage(context, fileName, "text/html") { outStream ->
                outStream.write(fullHtml.toByteArray(Charsets.UTF_8))
            }
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun exportToText(context: Context, title: String, content: String): Result<File> {
        return try {
            val fileName = "${title.replace(Regex("[^a-zA-Z0-9_-]"), "_")}_${System.currentTimeMillis()}.md"
            val file = saveFileToStorage(context, fileName, "text/markdown") { outStream ->
                outStream.write(content.toByteArray(Charsets.UTF_8))
            }
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun saveFileToStorage(
        context: Context,
        fileName: String,
        mimeType: String,
        writeBlock: (OutputStream) -> Unit
    ): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, fileName)
        FileOutputStream(file).use { out ->
            writeBlock(out)
        }
        return file
    }
}
