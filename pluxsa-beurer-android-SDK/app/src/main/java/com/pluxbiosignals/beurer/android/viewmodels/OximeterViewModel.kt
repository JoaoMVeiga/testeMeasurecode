package com.pluxbiosignals.beurer.android.viewmodels

import android.util.Log
import com.pluxbiosignals.beurer.android.model.Device
import com.pluxbiosignals.beurer.android.viewmodels.base.DeviceViewModel
import com.pluxbiosignals.beurer.api.States
import com.pluxbiosignals.beurer.api.communication.BeurerOximeter
import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication
import com.pluxbiosignals.beurer.api.interfaces.IOximeterCallback
import com.pluxbiosignals.beurer.api.objects.OximeterMeasure
import com.pluxbiosignals.beurer.api.objects.OximeterStorage
import com.pluxbiosignals.beurer.api.objects.OximeterTime
import com.pluxbiosignals.beurer.api.objects.OximeterVersion

class OximeterViewModel(communication: BeurerLECommunication, selectedDevice: Device) :
    DeviceViewModel(communication, selectedDevice), IOximeterCallback {

    private val TAG = this.javaClass.simpleName

    init {
        communication.init(this)
    }


    /*
    * COMMANDS
    * */
    fun getVersion() {
        (communication as BeurerOximeter).getVersion()
        updateDataField("")
    }

    fun getDeviceTime() {
        (communication as BeurerOximeter).getDeviceTime()
        updateDataField("")
    }

    fun setTimeSynchronization() {
        (communication as BeurerOximeter).setTimeSynchronization()
        updateDataField("")
    }

    fun getDataStorageInformation() {
        (communication as BeurerOximeter).getDataStorageInformation()
        updateDataField("")
    }

    fun getData() {
        (communication as BeurerOximeter).getMeasurementData(false)
        updateDataField("")
    }

    /*
    * DEVICE CALLBACKS
    * * */

    override fun onDeviceStateChanged(state: States?) {
        updateDeviceState(state)
    }

    override fun onData(measures: ArrayList<OximeterMeasure>?) {
        updateDataField(measures.toString())
    }

    override fun onSetTimeResult(success: Boolean) {
        updateDataField(if (success) "Successful" else "Failed")
    }

    override fun onVersion(version: OximeterVersion?) {
        updateDataField(version.toString())
    }

    override fun onTime(time: OximeterTime?) {
        updateDataField(time.toString())
    }

    override fun onStorageInfo(storage: OximeterStorage?) {
        updateDataField(storage.toString())
    }

}


