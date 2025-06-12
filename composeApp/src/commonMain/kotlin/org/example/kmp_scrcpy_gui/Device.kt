package org.example.kmp_scrcpy_gui

data class Device(
    val id: String,
    val model: String,
    val status: DeviceStatus
)

enum class DeviceStatus {
    ONLINE,
    OFFLINE,
    UNAUTHORIZED
}
