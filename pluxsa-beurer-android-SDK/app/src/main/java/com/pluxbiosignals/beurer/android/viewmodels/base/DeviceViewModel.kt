package com.pluxbiosignals.beurer.android.viewmodels.base

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pluxbiosignals.beurer.android.model.Device
import com.pluxbiosignals.beurer.android.viewmodels.GenericViewModel
import com.pluxbiosignals.beurer.android.viewmodels.GlucometerViewModel
import com.pluxbiosignals.beurer.android.viewmodels.OximeterViewModel
import com.pluxbiosignals.beurer.android.viewmodels.ScaleViewModel
import com.pluxbiosignals.beurer.api.States
import com.pluxbiosignals.beurer.api.communication.CommunicationFactory
import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication
import com.pluxbiosignals.beurer.api.enums.Devices
import com.pluxbiosignals.beurer.api.utils.Logger

open class DeviceViewModel(
    val communication: BeurerLECommunication,
    private val selectedDevice: Device
) : ViewModel() {

    private val TAG = this.javaClass.simpleName

    override fun onCleared() {
        super.onCleared()

        communication.finish()
    }


    //Device state
    private var _state: MutableState<States> = mutableStateOf(States.NO_CONNECTION)
    val state: State<States>
        get() = _state

    //Data
    private var _data: MutableState<String> = mutableStateOf("")
    val data: State<String>
        get() = _data

    fun connectDevice() {

        if (state.value == States.CONNECTED) {
            return
        }
        Logger.d(TAG, "Connect device  ${selectedDevice.macAddress}. Current state ${state.value}")
        Log.e("Testes", "Connect device  ${selectedDevice.macAddress}. Current state ${state.value}")


        try {
            communication.connect(selectedDevice.macAddress)
            Log.e("Testes", "Sucesso try catch")
        } catch (e: Exception) {
            Logger.e(TAG, "Unable to connect", e)
            Log.e("Testes", "Unable to connect", e)
        }
    }

    fun disconnectDevice() {
        Logger.d(
            TAG,
            "Disconnect device  ${selectedDevice.macAddress}. Current state ${state.value}"
        )
        Log.e("Testes", "Disconnect device  ${selectedDevice.macAddress}. Current state ${state.value}")

        Logger.d(
            TAG,
            "Disconnect device  ${selectedDevice.macAddress}. Current state ${_state.value}"
        )
        Log.e("Testes", "Disconnect device  ${selectedDevice.macAddress}. Current state ${_state.value}")

        try {
            communication.disconnect()
        } catch (e: Exception) {
            Logger.e(TAG, "Unable to disconnect", e)
            Log.e("Testes", "Unable to disconnect", e)
        }
    }

    protected fun updateDataField(data: String?) {
        if (data != null) {
            _data.value = data
        }
    }


    protected fun updateDeviceState(state: States?) {
        Logger.d(TAG, "Device state changed -> $state")

        if (state != null) {
            _state.value = state
        }
    }

}

class DeviceViewModelFactory(
    private val selectedDevice: Device,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val communication: BeurerLECommunication = CommunicationFactory
            .getCommunication(selectedDevice.type, context);

        when (selectedDevice.type) {
            Devices.GLUCOMETER ->
                if (modelClass.isAssignableFrom(GlucometerViewModel::class.java)) {
                    return GlucometerViewModel(communication, selectedDevice) as T
                }

            Devices.OXIMETER -> if (modelClass.isAssignableFrom(OximeterViewModel::class.java)) {
                return OximeterViewModel(communication, selectedDevice) as T
            }

            Devices.TEMP,  Devices.TEMP_BASAL, Devices.BPM -> if (modelClass.isAssignableFrom(GenericViewModel::class.java)) {
                return GenericViewModel(communication, selectedDevice) as T
            }


            Devices.SCALE -> if (modelClass.isAssignableFrom(ScaleViewModel::class.java)) {
                return ScaleViewModel(communication, selectedDevice) as T
            }

            else -> {}
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


