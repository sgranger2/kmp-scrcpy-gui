package org.example.kmp_scrcpy_gui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DeviceList(
    devices: List<Device>,
    onDeviceSelected: (Device) -> Unit
) {
    LazyColumn {
        items(devices) { device ->
            DeviceItem(
                device = device,
                onClick = { onDeviceSelected(device) }
            )
        }
    }
}

@Composable
fun DeviceItem(
    device: Device,
    onClick: () -> Unit
) {
    val statusColor = when (device.status) {
        DeviceStatus.ONLINE -> MaterialTheme.colorScheme.primary
        DeviceStatus.OFFLINE -> MaterialTheme.colorScheme.error
        DeviceStatus.UNAUTHORIZED -> MaterialTheme.colorScheme.secondary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(device.model, style = MaterialTheme.typography.titleMedium)
            Text(
                device.id,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    device.status.name,
                    color = statusColor,
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = onClick,
                    enabled = device.status == DeviceStatus.ONLINE
                ) {
                    Text("Connect")
                }
            }
        }
    }
}