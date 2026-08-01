package com.example.ui.screens.benchmark

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
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
import com.example.ui.components.NeobrutalCard
import com.example.ui.components.NeobrutalStatusBadge
import com.example.ui.viewmodel.HardwareSpec

@Composable
fun HardwareSpecCard(
    spec: HardwareSpec,
    modifier: Modifier = Modifier
) {
    NeobrutalCard(
        backgroundColor = Color.White, // Always make background white
        borderColor = Color.Black,
        shadowColor = Color.Black,
        borderWidth = 2.dp,
        shadowOffset = 4.dp,
        cornerRadius = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Icon header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PhoneAndroid,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "HARDWARE SPEC DETECTED",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = spec.cpuModel.uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color.Black
            )
            Text(
                text = "ARCHITECTURE: ${spec.cpuArchitecture.uppercase()} • ${spec.coreCount} PHYSICAL CORES",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "SIMD & MATRIX EXTENSIONS:",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color.Black
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 6.dp)
            ) {
                spec.simdFeatures.forEach { feature ->
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF7DD3FC)) // Sky blue feature tag
                            .border(width = 1.dp, color = Color.Black)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = feature.uppercase(),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "SYSTEM RAM: ${spec.ramSizeGb} GB",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black
                )
                Text(
                    text = "STORAGE FREE: ${spec.availableStorageGb} GB",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black
                )
            }
        }
    }
}
