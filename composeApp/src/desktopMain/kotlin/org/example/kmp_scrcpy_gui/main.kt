package org.example.kmp_scrcpy_gui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.kmp_scrcpy_gui.ui.AppNavHost

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMP Scrcpy GUI",
    ) {
        AppNavHost()
    }
}