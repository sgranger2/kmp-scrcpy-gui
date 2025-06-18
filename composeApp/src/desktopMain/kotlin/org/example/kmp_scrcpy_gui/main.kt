package org.example.kmp_scrcpy_gui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.remember
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.*
import org.example.kmp_scrcpy_gui.ui.AppNavHost
import org.example.kmp_scrcpy_gui.utils.MinimizeFixUtils
import java.awt.Frame

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Scrcpy-GUI",
        state = WindowState(
            width = 480.dp,
            height = 550.dp
        ),
        undecorated = true // Remove OS title bar
    ) {
        // Fix for minimize button not working on Windows
        val windowHandle = remember(this.window) {
            val windowPointer = (this.window as? ComposeWindow)
                ?.windowHandle
                ?.let(::Pointer)
                ?: Native.getWindowPointer(this.window)
            HWND(windowPointer)
        }
        remember(windowHandle) { MinimizeFixUtils.CustomWindowProcedure(windowHandle) }

        // Display the custom title bar and main content
        val window = this.window
        MaterialTheme(colorScheme = darkColorScheme()) {
            Column {
                CustomTitleBar(
                    title = "Scrcpy-GUI",
                    window = window,
                    onCloseRequest = ::exitApplication,
                    onMinimizeRequest = { User32.INSTANCE.CloseWindow(windowHandle) },
                    onMaximizeRequest = {
                        window.extendedState = if (window.extendedState == Frame.MAXIMIZED_BOTH) {
                            Frame.NORMAL
                        } else Frame.MAXIMIZED_BOTH
                    }
                )
                AppNavHost()
            }
        }
    }
}
