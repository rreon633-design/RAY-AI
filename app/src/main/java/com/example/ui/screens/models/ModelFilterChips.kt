package com.example.ui.screens.models

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ModelCategory

@Composable
fun ModelFilterChips(
    selectedCategory: ModelCategory?,
    onSelectCategory: (ModelCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        Pair("ALL MODELS", null),
        Pair("ULTRA FAST", ModelCategory.ULTRA_FAST),
        Pair("BALANCED", ModelCategory.BALANCED),
        Pair("HIGH QUALITY", ModelCategory.HIGH_QUALITY)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(filters) { (label, category) ->
            val isSelected = selectedCategory == category
            val bgColor = if (isSelected) Color(0xFFF4D03F) else Color.White // Neobrutal yellow or white
            
            Box(
                modifier = Modifier
                    .background(bgColor)
                    .border(width = 2.dp, color = Color.Black)
                    .clickable { onSelectCategory(category) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }
        }
    }
}
