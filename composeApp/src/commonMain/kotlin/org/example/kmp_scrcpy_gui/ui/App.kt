package org.example.kmp_scrcpy_gui.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.kmp_scrcpy_gui.data.model.DeviceStatus
import org.example.kmp_scrcpy_gui.data.DeviceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    viewModel: DeviceViewModel,
    settings: Map<String, String>,
    onNavigateToSettings: () -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.refreshDevices()
    }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text("scrcpy GUI") },
                actions = {
                    // Refresh button
                    IconButton(onClick = {
                        scope.launch {
                            viewModel.refreshDevices()
                        }
                    }) {
                        Text("🔄")
                    }

                    // Settings button
                    IconButton(onClick = onNavigateToSettings) {
                        Text("⚙️")
                    }
                }
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                if (devices.isEmpty()) {
                    Text(
                        "No devices connected",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    DeviceList(
                        devices = devices,
                        onDeviceSelected = { device ->
                            if (device.status == DeviceStatus.ONLINE) {
                                // Now using the passed settings
                                viewModel.launchScrcpy(device.id, settings)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text("scrcpy must be installed and in PATH", style = MaterialTheme.typography.bodySmall)
        }
    }
}
