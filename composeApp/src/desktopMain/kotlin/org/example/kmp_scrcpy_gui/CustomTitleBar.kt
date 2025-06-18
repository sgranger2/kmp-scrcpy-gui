package org.example.kmp_scrcpy_gui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kmp_scrcpy_gui.composeapp.generated.resources.Res
import kmp_scrcpy_gui.composeapp.generated.resources.compose_multiplatform
import kmp_scrcpy_gui.composeapp.generated.resources.icons8_android_os_100
import kmp_scrcpy_gui.composeapp.generated.resources.icons8_horizontal_line_30
import kmp_scrcpy_gui.composeapp.generated.resources.icons8_square_32
import kmp_scrcpy_gui.composeapp.generated.resources.icons8_x_48
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import java.awt.Window
import java.awt.Point
import java.awt.MouseInfo

@Composable
fun CustomTitleBar(
    title: String,
    window: Window,
    onCloseRequest: () -> Unit,
    onMinimizeRequest: () -> Unit,
    onMaximizeRequest: () -> Unit
) {
    var dragOffset by remember { mutableStateOf(Point(0, 0)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colorScheme.onSecondary)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        val mousePos = MouseInfo.getPointerInfo().location
                        val windowPos = window.location
                        dragOffset = Point(mousePos.x - windowPos.x, mousePos.y - windowPos.y)
                    },
                    onDrag = { _, _ ->
                        val mousePos = MouseInfo.getPointerInfo().location
                        window.setLocation(mousePos.x - dragOffset.x, mousePos.y - dragOffset.y)
                    }
                )
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.icons8_android_os_100),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 8.dp, top = 6.dp),
                tint = Color.Unspecified
            )
            Text(title, color = Color.White, modifier = Modifier.padding(start = 8.dp))
        }
        Row {
            IconButton(onClick = onMinimizeRequest) {
                Icon(
                    painterResource(Res.drawable.icons8_horizontal_line_30),
                    contentDescription = "Minimize",
                    modifier = Modifier.size(13.dp),
                    tint = Color.White,
                )
            }
            IconButton(onClick = onMaximizeRequest) {
                Icon(
                    painterResource(Res.drawable.icons8_square_32),
                    contentDescription = "Maximize",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
            IconButton(onClick = onCloseRequest) {
                Icon(
                    painterResource(Res.drawable.icons8_x_48),
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
