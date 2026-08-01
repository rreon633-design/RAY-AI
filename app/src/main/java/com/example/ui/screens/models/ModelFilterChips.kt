package com.example.ui.screens.models

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ModelCategory
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.OnAccentPurple
import com.example.ui.theme.TextSecondary

@Composable
fun ModelFilterChips(
    selectedCategory: ModelCategory?,
    onSelectCategory: (ModelCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        Pair("All Models", null),
        Pair("⚡ Ultra Fast (<1GB)", ModelCategory.ULTRA_FAST),
        Pair("⚖️ Balanced (1-1.5GB)", ModelCategory.BALANCED),
        Pair("🧠 High Quality (>1.5GB)", ModelCategory.HIGH_QUALITY)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(filters) { (label, category) ->
            val isSelected = selectedCategory == category
            val bgColor = if (isSelected) AccentPurple else ImmersiveSurfaceVariant
            val textColor = if (isSelected) OnAccentPurple else TextSecondary

            Box(
                modifier = Modifier
                    .background(bgColor, shape = RoundedCornerShape(20.dp))
                    .border(
                        1.dp,
                        if (isSelected) AccentPurple else ImmersiveBorder,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelectCategory(category) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = textColor
                )
            }
        }
    }
}

