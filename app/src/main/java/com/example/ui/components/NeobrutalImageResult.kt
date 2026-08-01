package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NeobrutalImageResult(
    prompt: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLiked by remember { mutableStateOf<Boolean?>(null) }
    
    NeobrutalCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderColor = Color.Black,
        borderWidth = 2.dp,
        shadowOffset = 4.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "RAY AI ART GENERATOR",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Generated visual for: \"$prompt\"",
                fontSize = 12.sp,
                color = Color.Black,
                lineHeight = 16.sp
            )
            
            Spacer(modifier = Modifier.height(10.dp))
            
            NeobrutalCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                backgroundColor = Color(0xFF1E293B),
                borderColor = Color.Black,
                borderWidth = 1.5.dp,
                shadowOffset = 2.dp
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        
                        for (i in 0..10) {
                            val y = height * (i / 10f)
                            drawLine(
                                color = Color(0xFFEC4899).copy(alpha = 0.2f),
                                start = Offset(0f, y),
                                end = Offset(width, y),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                        
                        drawCircle(
                            color = Color(0xFFF4D03F),
                            radius = 35.dp.toPx(),
                            center = Offset(width / 2f, height / 2f)
                        )
                        
                        val buildingWidth = width / 5f
                        val heights = listOf(0.4f, 0.6f, 0.5f, 0.7f, 0.3f)
                        heights.forEachIndexed { idx, hFactor ->
                            val left = idx * buildingWidth
                            val top = height * (1f - hFactor)
                            drawRect(
                                color = Color(0xFF0F172A),
                                topLeft = Offset(left, top),
                                size = Size(buildingWidth, height * hFactor)
                            )
                            val winCols = 3
                            val winRows = 4
                            for (r in 0 until winRows) {
                                for (c in 0 until winCols) {
                                    val winX = left + (buildingWidth / (winCols + 1)) * (c + 1)
                                    val winY = top + (height * hFactor / (winRows + 1)) * (r + 1)
                                    drawCircle(
                                        color = if ((idx + r + c) % 2 == 0) Color(0xFF06B6D4) else Color(0xFFEC4899),
                                        radius = 2.dp.toPx(),
                                        center = Offset(winX, winY)
                                    )
                                }
                            }
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "SEED: 9283741 | CFG: 7.5",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF4D03F))
                        .border(2.dp, Color.Black)
                        .clickable {
                            Toast.makeText(context, "Upscaling image to 4K resolution...", Toast.LENGTH_SHORT).show()
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "UPSCALE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White)
                        .border(2.dp, Color.Black)
                        .clickable {
                            Toast.makeText(context, "Generating variations based on seed...", Toast.LENGTH_SHORT).show()
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VARIATIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF6BB6EC))
                        .border(2.dp, Color.Black)
                        .clickable {
                            Toast.makeText(context, "Saved generated visual to Gallery!", Toast.LENGTH_SHORT).show()
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SAVE ART",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SIZE: 1024x1024 | RATIO: 1:1",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isLiked == true) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "Like",
                        tint = if (isLiked == true) Color(0xFF0D7BB3) else Color.Black,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { isLiked = if (isLiked == true) null else true }
                    )
                    Icon(
                        imageVector = if (isLiked == false) Icons.Default.ThumbDown else Icons.Outlined.ThumbDown,
                        contentDescription = "Dislike",
                        tint = if (isLiked == false) Color.Red else Color.Black,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { isLiked = if (isLiked == false) null else false }
                    )
                }
            }
        }
    }
}
