package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeobrutalStatusBadge

@Composable
fun SettingsHeader(
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Hamburger Icon inside Neobrutal Box
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, Color.Black)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black
                )
            }

            // Title "RAY AI" in center/left-aligned
            Text(
                text = "RAY AI",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )

            // Right ACTIVE tag/badge in yellow Neobrutal style
            NeobrutalStatusBadge(
                text = "ACTIVE",
                backgroundColor = Color(0xFFF4D03F)
            )
        }

        // Bold bottom black line (4dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.Black)
        )
    }
}
