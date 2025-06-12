package org.example.kmp_scrcpy_gui

class JVMPlatform: Platform {
    override val name: String = "Desktop"
}

actual fun getPlatform(): Platform = JVMPlatform()