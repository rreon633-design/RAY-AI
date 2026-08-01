package com.example.ui.screens.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.OnAccentPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ThreadSelector(
    selectedThreads: Int,
    onThreadSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, ImmersiveBorder, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "CPU Core Thread Count",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Higher threads increase tokens/sec speed but consume more battery",
                fontSize = 12.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(1, 2, 4, 6, 8).forEach { thread ->
                    FilterChip(
                        selected = selectedThreads == thread,
                        onClick = { onThreadSelected(thread) },
                        label = { Text("$thread Cores") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentPurple,
                            selectedLabelColor = OnAccentPurple,
                            containerColor = ImmersiveSurface,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }
    }
}

