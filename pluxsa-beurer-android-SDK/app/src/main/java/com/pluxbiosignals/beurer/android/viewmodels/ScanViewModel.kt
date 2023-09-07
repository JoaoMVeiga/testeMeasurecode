package com.pluxbiosignals.beurer.android.viewmodels

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pluxbiosignals.beurer.android.model.Device
import com.pluxbiosignals.beurer.api.DeviceScan
import com.pluxbiosignals.beurer.api.enums.Devices
import com.pluxbiosignals.beurer.api.interfaces.IDeviceFound
import com.pluxbiosignals.beurer.api.utils.Logger

class ScanViewModel(private val deviceScanner: DeviceScan) : ViewModel(), IDeviceFound {

    private val TAG = this.javaClass.simpleName

    init {
        Logger.v(TAG, "Initializing view model")

        deviceScanner.setCallback(this)
    }

    override fun onCleared() {
        super.onCleared()

        Logger.v(TAG, "Scan view model cleared")

    }

    private var _isScanning = mutableStateOf(false)
    val isScanning: State<Boolean>
        get() = _isScanning

    private val _devices = mutableStateListOf<Device>()
    val devices: List<Device>
        get() = _devices

    fun startScan() {
        clearDevices()
        Logger.v(TAG, "Start scan...")

        deviceScanner.startScan()
        _isScanning.value = true
    }

    fun stopScan() {
        if (!_isScanning.value) {
            return
        }

        Logger.v(TAG, "Stop scan...")

        deviceScanner.stopScan()
        _isScanning.value = false
    }

    fun clearDevices() {
        _devices.clear()
    }

    @SuppressLint("MissingPermission")
    override fun onDeviceFound(bluetoothDevice: BluetoothDevice?, type: Devices) {
        if (bluetoothDevice == null) {
            return
        }

        if (!_devices.map { it.macAddress }.contains(bluetoothDevice.address)) {
            _devices.add(Device(bluetoothDevice.address, name = bluetoothDevice.name, type = type))
            Logger.d(TAG, "Add device " + bluetoothDevice.address)
        }
    }
}


class ScanViewModelFactory(private val scanner: DeviceScan) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            return ScanViewModel(deviceScanner = scanner) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}