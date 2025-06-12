package org.example.kmp_scrcpy_gui.data

import java.io.File
import java.util.Properties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsStore {
    private val settingsFile = File(System.getProperty("user.home"), ".scrcpygui/settings.properties")

    init {
        settingsFile.parentFile?.mkdirs()
    }

    suspend fun saveSettings(settings: Map<String, String>) = withContext(Dispatchers.IO) {
        val properties = Properties()
        settings.forEach { (key, value) ->
            properties.setProperty(key, value)
        }

        settingsFile.outputStream().use {
            properties.store(it, "scrcpy GUI Settings")
        }
    }

    suspend fun loadSettings(): Map<String, String> = withContext(Dispatchers.IO) {
        val properties = Properties()

        if (settingsFile.exists()) {
            settingsFile.inputStream().use {
                properties.load(it)
            }
        }

        val result = mutableMapOf<String, String>()
        properties.forEach { key, value ->
            result[key.toString()] = value.toString()
        }

        // Set defaults if not present
        if ("bitrate" !in result) result["bitrate"] = "8M"
        if ("maxSize" !in result) result["maxSize"] = "1080"
        if ("maxFps" !in result) result["maxFps"] = "60"
        if ("noAudio" !in result) result["noAudio"] = "false"
        if ("stayAwake" !in result) result["stayAwake"] = "true"

        result
    }
}