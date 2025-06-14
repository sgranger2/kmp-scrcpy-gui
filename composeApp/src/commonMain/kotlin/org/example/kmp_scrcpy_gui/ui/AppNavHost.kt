package org.example.kmp_scrcpy_gui.ui

import androidx.compose.runtime.*
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

    when (currentScreen) {
        Screen.DEVICES -> {
            App(
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