package org.example.kmp_scrcpy_gui.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.example.kmp_scrcpy_gui.data.model.Device

class DeviceViewModel(private val scrcpyService: ScrcpyService) {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun refreshDevices() {
        scope.launch {
            _isLoading.value = true
            scrcpyService.getConnectedDevices().collect { deviceList ->
                _devices.value = deviceList
                _isLoading.value = false
            }
        }
    }

    fun launchScrcpy(deviceId: String, options: Map<String, String>) {
        scope.launch {
            try {
                scrcpyService.launchScrcpy(deviceId, options)
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }
}