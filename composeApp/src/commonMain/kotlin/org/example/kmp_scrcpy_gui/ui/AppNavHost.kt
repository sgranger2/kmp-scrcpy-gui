package org.example.kmp_scrcpy_gui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.example.kmp_scrcpy_gui.data.DeviceViewModel
import org.example.kmp_scrcpy_gui.data.ScrcpyService
import org.example.kmp_scrcpy_gui.data.SettingsStore

enum class Screen {
    DEVICES,
    SETTINGS
}

@Composable
fun AppNavHost() {
    val viewModel = remember { DeviceViewModel(ScrcpyService()) }
    val scope = rememberCoroutineScope()
    val settingsStore = remember { SettingsStore() }
    var currentScreen by remember { mutableStateOf(Screen.DEVICES) }
    var currentSettings by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    LaunchedEffect(Unit) {
        currentSettings = settingsStore.loadSettings()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        when (currentScreen) {
            Screen.DEVICES -> {
                HomeScreen(
                    viewModel = viewModel,
                    settings = currentSettings,
                    onNavigateToSettings = {
                        currentScreen = Screen.SETTINGS
                    }
                )
            }

            Screen.SETTINGS -> {
                SettingsScreen(
                    initialSettings = currentSettings,
                    onSave = { newSettings ->
                        scope.launch {
                            settingsStore.saveSettings(newSettings)
                            currentSettings = newSettings
                            currentScreen = Screen.DEVICES
                        }
                    },
                    onCancel = {
                        currentScreen = Screen.DEVICES
                    }
                )
            }
        }
    }
}