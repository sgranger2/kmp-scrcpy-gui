package org.example.kmp_scrcpy_gui

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.example.kmp_scrcpy_gui.ui.AppNavHost

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Scrcpy GUI",
        state = WindowState(
            width = 480.dp,
            height = 550.dp
        )
    ) {
        AppNavHost()
    }
}