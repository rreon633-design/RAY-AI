package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NeobrutalChart(
    title: String,
    bars: List<Pair<String, Double>>,
    modifier: Modifier = Modifier
) {
    val maxVal = bars.maxOfOrNull { it.second } ?: 1.0
    
    NeobrutalCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderColor = Color.Black,
        borderWidth = 2.dp,
        shadowOffset = 4.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title.uppercase().trim(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(10.dp))
            
            bars.forEach { (label, value) ->
                val fraction = if (maxVal > 0) (value / maxVal).toFloat() else 0f
                
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label.trim(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black
                        )
                        Text(
                            text = "${value.toInt()}B",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF0D7BB3)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(Color(0xFFEEEEEE))
                            .border(1.5.dp, Color.Black)
                    ) {
                        if (fraction > 0f) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction)
                                    .fillMaxHeight()
                                    .background(Color(0xFFF4D03F))
                            ) {
                                if (fraction < 1f) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(1.5.dp)
                                            .background(Color.Black)
                                            .align(Alignment.CenterEnd)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
