package org.example.kmp_scrcpy_gui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class ScrcpyService {
    fun getConnectedDevices(): Flow<List<Device>> = flow {
        val process = withContext(Dispatchers.IO) {
            ProcessBuilder("adb", "devices").start()
        }

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val errorReader = BufferedReader(InputStreamReader(process.errorStream))
        val devices = mutableListOf<Device>()

        // Collect all output for debugging
        val outputLines = mutableListOf<String>()
        var line: String?
        // Skip the first line (header)
        reader.readLine()?.let { outputLines.add(it) }
        while (reader.readLine().also { line = it } != null) {
            outputLines.add(line!!)
            line?.let {
                if (it.isNotBlank()) {
                    val parts = it.split("\\s+".toRegex())
                    if (parts.size >= 2) {
                        val id = parts[0]
                        val status = when (parts[1]) {
                            "device" -> DeviceStatus.ONLINE
                            "offline" -> DeviceStatus.OFFLINE
                            else -> DeviceStatus.UNAUTHORIZED
                        }
                        devices.add(Device(id, getDeviceModel(id), status))
                    }
                }
            }
        }

        // Log adb output
        println("[ADB OUTPUT]" + outputLines.joinToString("\n"))
        // Log adb errors
        val errorOutput = errorReader.readText()
        if (errorOutput.isNotBlank()) {
            println("[ADB ERROR] $errorOutput")
        }

        process.waitFor()
        emit(devices)
    }
        .flowOn(Dispatchers.IO)

    private suspend fun getDeviceModel(deviceId: String): String {
        val process = withContext(Dispatchers.IO) {
            ProcessBuilder("adb", "-s", deviceId, "shell", "getprop", "ro.product.model").start()
        }
        return BufferedReader(InputStreamReader(process.inputStream)).use { it.readLine() ?: "Unknown" }
    }

    suspend fun launchScrcpy(deviceId: String, options: Map<String, String>): Process {
        val command = mutableListOf("scrcpy", "-s", deviceId)

        options.forEach { (key, value) ->
            when (key) {
                "bitrate" -> command.addAll(listOf("--video-bit-rate", value))
                "maxSize" -> command.addAll(listOf("--max-size", value))
                "maxFps" -> command.addAll(listOf("--max-fps", value))
                "noAudio" -> if (value.toBoolean()) command.add("--no-audio")
                "stayAwake" -> if (value.toBoolean()) command.add("--stay-awake")
                // Add other options as needed
            }
        }

        println("[SCRCPY COMMAND] ${command.joinToString(" ")}")

        return withContext(Dispatchers.IO) {
            val process = ProcessBuilder(command).start()

            // Print output and error streams for debugging
            Thread {
                process.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { println("[SCRCPY OUTPUT] $it") }
                }
            }.start()
            Thread {
                process.errorStream.bufferedReader().useLines { lines ->
                    lines.forEach { println("[SCRCPY ERROR] $it") }
                }
            }.start()

            process
        }
    }
}