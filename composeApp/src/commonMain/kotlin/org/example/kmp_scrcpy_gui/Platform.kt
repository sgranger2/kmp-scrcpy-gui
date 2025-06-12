package org.example.kmp_scrcpy_gui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform