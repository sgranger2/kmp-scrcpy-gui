package org.example.kmp_scrcpy_gui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    initialSettings: Map<String, String>,
    onSave: (Map<String, String>) -> Unit,
    onCancel: () -> Unit
) {
    var bitrate by remember { mutableStateOf(initialSettings["bitrate"] ?: "8M") }
    var maxSize by remember { mutableStateOf(initialSettings["maxSize"] ?: "1080") }
    var maxFps by remember { mutableStateOf(initialSettings["maxFps"] ?: "60") }
    var noAudio by remember { mutableStateOf((initialSettings["noAudio"] ?: "false").toBoolean()) }
    var stayAwake by remember { mutableStateOf((initialSettings["stayAwake"] ?: "true").toBoolean()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("scrcpy Settings", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = bitrate,
            onValueChange = { bitrate = it },
            label = { Text("Bitrate (e.g. 8M)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = maxSize,
            onValueChange = { maxSize = it },
            label = { Text("Max Size (e.g. 1080)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = maxFps,
            onValueChange = { maxFps = it },
            label = { Text("Max FPS (e.g. 60)") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = noAudio,
                onCheckedChange = { noAudio = it }
            )
            Text("Disable Audio")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = stayAwake,
                onCheckedChange = { stayAwake = it }
            )
            Text("Keep Device Awake")
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    val settings = mapOf(
                        "bitrate" to bitrate,
                        "maxSize" to maxSize,
                        "maxFps" to maxFps,
                        "noAudio" to noAudio.toString(),
                        "stayAwake" to stayAwake.toString()
                    )
                    onSave(settings)
                }
            ) {
                Text("Save")
            }
        }
    }
}