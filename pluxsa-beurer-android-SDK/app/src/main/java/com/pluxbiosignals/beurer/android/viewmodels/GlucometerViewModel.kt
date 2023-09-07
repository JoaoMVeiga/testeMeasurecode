package com.pluxbiosignals.beurer.android.viewmodels

import com.pluxbiosignals.beurer.android.model.Device
import com.pluxbiosignals.beurer.android.viewmodels.base.DeviceViewModel
import com.pluxbiosignals.beurer.api.States
import com.pluxbiosignals.beurer.api.communication.BeurerGlucometer
import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication
import com.pluxbiosignals.beurer.api.interfaces.IGlucCallback
import com.pluxbiosignals.beurer.api.objects.GlucContext
import com.pluxbiosignals.beurer.api.objects.GlucMeasurement

class GlucometerViewModel(communication: BeurerLECommunication, selectedDevice: Device) :
    DeviceViewModel(communication, selectedDevice), IGlucCallback {

    private val TAG = this.javaClass.simpleName

    init {
        communication.init(this)
    }


    /*
    * COMMANDS
    * */

    fun getAllRecords() {
        (communication as BeurerGlucometer).getAllRecords()
        updateDataField("")
    }

    fun getFirstRecord() {
        (communication as BeurerGlucometer).getFirstRecord()
        updateDataField("")
    }

    fun getLastRecord() {
        (communication as BeurerGlucometer).getLastRecord()
        updateDataField("")
    }

    fun getLowerThan() {
        (communication as BeurerGlucometer).getRecordsLessThenOrEqualTo(90)
        updateDataField("")
    }

    fun getHigherThan() {
        (communication as BeurerGlucometer).getRecordsGreaterThenOrEqualTo(80)
        updateDataField("")
    }

    fun getWithin() {
        (communication as BeurerGlucometer).getStoredRecordsFromRange(80, 90)
        updateDataField("")
    }

    fun getTotalRecords() {
        (communication as BeurerGlucometer).getNumberOfStoredRecords()
        updateDataField("")
    }

    /*
    * DEVICE CALLBACKS
    * * * */

    override fun onDeviceStateChanged(state: States?) {
        updateDeviceState(state)
    }

    override fun onResults(
        measurements: ArrayList<GlucMeasurement>?,
        contexts: ArrayList<GlucContext>?
    ) {
        val results: String = measurements.toString() + contexts.toString()

        updateDataField(results)
    }

    override fun onTotalRecords(total: Int) {
        updateDataField("Total records: $total")
    }
}



