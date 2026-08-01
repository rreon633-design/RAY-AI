package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun NotificationsSection(
    downloadAlerts: Boolean,
    onDownloadAlertsChanged: (Boolean) -> Unit,
    dailyBrief: Boolean,
    onDailyBriefChanged: (Boolean) -> Unit,
    usageWarnings: Boolean,
    onUsageWarningsChanged: (Boolean) -> Unit,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(PurpleContainer, shape = RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = AccentPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Notifications & Alerts",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Manage system notices and model progress alerts",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                NotificationToggleRow(
                    title = "Model Download Completion",
                    subtitle = "Notify when background INT4 model download finishes",
                    checked = downloadAlerts,
                    onCheckedChange = onDownloadAlertsChanged
                )

                NotificationToggleRow(
                    title = "High RAM & CPU Usage Warning",
                    subtitle = "Alert if context length exceeds device memory limit",
                    checked = usageWarnings,
                    onCheckedChange = onUsageWarningsChanged
                )

                NotificationToggleRow(
                    title = "Daily Morning AI Brief",
                    subtitle = "Receive a daily summary recommendation on device",
                    checked = dailyBrief,
                    onCheckedChange = onDailyBriefChanged
                )
            }
        }
    }
}

@Composable
private fun NotificationToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(ImmersiveSurface, shape = RoundedCornerShape(12.dp))
            .border(1.dp, ImmersiveBorder, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(text = subtitle, fontSize = 11.sp, color = TextMuted)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AccentPurple,
                checkedTrackColor = PurpleContainer
            )
        )
    }
}
