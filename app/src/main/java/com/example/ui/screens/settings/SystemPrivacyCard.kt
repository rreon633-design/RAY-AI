package com.example.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeobrutalButton
import com.example.ui.components.NeobrutalCard

@Composable
fun SystemPrivacyCard(
    onExportHistory: () -> Unit,
    onDeleteData: () -> Unit,
    modifier: Modifier = Modifier
) {
    NeobrutalCard(
        backgroundColor = Color(0xFFFADBD8), // Soft pink/red background matching mockup
        shadowOffset = 6.dp,
        borderWidth = 2.5.dp,
        cornerRadius = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Section Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFF922B21), // Dark red icon
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "System & Privacy",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(0xFF78281F) // Dark brown-red title matching mockup
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Export Chat History Button (White Neobrutal card)
            NeobrutalButton(
                onClick = onExportHistory,
                backgroundColor = Color.White,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Export Chat History",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )

                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Export",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delete Account & Data Button (Dark crimson neobrutal card)
            NeobrutalButton(
                onClick = onDeleteData,
                backgroundColor = Color(0xFFB03A2E), // Dark Red matching mockup
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Delete Account & Data",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
