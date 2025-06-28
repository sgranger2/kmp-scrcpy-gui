package org.example.kmp_scrcpy_gui.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import org.example.kmp_scrcpy_gui.data.DeviceViewModel
import org.example.kmp_scrcpy_gui.data.ScrcpyService
import org.example.kmp_scrcpy_gui.data.SettingsStore

enum class Screen {
    DEVICES,
    SETTINGS
}

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7AA2F7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3B4261),
    onPrimaryContainer = Color(0xFFCAD3F5),

    secondary = Color(0xFFBB9AF7),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3B4261),
    onSecondaryContainer = Color(0xFFE0D5F7),

    tertiary = Color(0xFF9ECE6A),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF3B4261),
    onTertiaryContainer = Color(0xFFD5E8C5),

    error = Color(0xFFF7768E),
    onError = Color.White,
    errorContainer = Color(0xFF3B4261),
    onErrorContainer = Color(0xFFFFC9D3),

    background = Color(0xFF1A1B26),
    onBackground = Color(0xFFCAD3F5),

    surface = Color(0xFF2A2E3F),
    onSurface = Color(0xFFCAD3F5),
    surfaceVariant = Color(0xFF3B4261),
    onSurfaceVariant = Color(0xFF9CA3AF),

    outline = Color(0xFF3B4261),
    outlineVariant = Color(0xFF2A2E3F),

    surfaceTint = Color(0xFF7AA2F7)
)

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

    MaterialTheme(
        colorScheme = DarkColorScheme
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Simplified without complex animations for now
            Box(modifier = Modifier.fillMaxSize()) {
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
    }
}