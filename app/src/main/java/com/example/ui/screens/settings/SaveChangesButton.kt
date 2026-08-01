package com.example.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
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

@Composable
fun SaveChangesButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NeobrutalButton(
        onClick = onClick,
        backgroundColor = Color(0xFFF4D03F), // Neobrutal yellow matching mockup
        borderWidth = 2.5.dp,
        shadowOffset = 6.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SAVE CHANGES",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save Icon",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
