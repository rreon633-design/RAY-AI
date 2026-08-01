package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
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
fun NeobrutalTable(
    headers: List<String>,
    rows: List<List<String>>,
    modifier: Modifier = Modifier
) {
    NeobrutalCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderColor = Color.Black,
        borderWidth = 2.dp,
        shadowOffset = 4.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 8.dp, horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                headers.forEach { header ->
                    Text(
                        text = header.uppercase().trim(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            HorizontalDivider(color = Color.Black, thickness = 2.dp)
            
            // Data Rows
            rows.forEachIndexed { rowIndex, row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (rowIndex % 2 == 0) Color.White else Color(0xFFF9F9F9))
                        .padding(vertical = 8.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Make sure cells row match the columns number
                    val paddedRow = row + List(maxOf(0, headers.size - row.size)) { "" }
                    paddedRow.take(headers.size).forEach { cell ->
                        Text(
                            text = cell.trim(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (rowIndex < rows.size - 1) {
                    HorizontalDivider(color = Color.Black, thickness = 1.dp)
                }
            }
        }
    }
}
