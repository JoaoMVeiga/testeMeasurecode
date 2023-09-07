/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.android.viewmodels

import android.util.Log
import com.pluxbiosignals.beurer.android.model.Device
import com.pluxbiosignals.beurer.android.viewmodels.base.DeviceViewModel
import com.pluxbiosignals.beurer.api.States
import com.pluxbiosignals.beurer.api.communication.BeurerWeightScale
import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication
import com.pluxbiosignals.beurer.api.enums.scale.MeasurementStatus
import com.pluxbiosignals.beurer.api.interfaces.IScaleCallback
import com.pluxbiosignals.beurer.api.objects.ScaleBodyComposition
import com.pluxbiosignals.beurer.api.objects.ScaleMeasurement
import com.pluxbiosignals.beurer.api.objects.ScaleUser

class ScaleViewModel(communication: BeurerLECommunication, selectedDevice: Device) :
    DeviceViewModel(communication, selectedDevice), IScaleCallback {


    private val TAG = this.javaClass.simpleName

    init {
        communication.init(this)
    }


    /*
    * COMMANDS
    * */
    fun setTime() {
        (communication as BeurerWeightScale).setCurrentTime()
        updateDataField("")
    }

    fun listUsers() {
        (communication as BeurerWeightScale).listAllUsers()
        updateDataField("")
    }

    fun takeMeasurement() {
        (communication as BeurerWeightScale).takeMeasurement()
        updateDataField("")
    }

    fun createUser(user: ScaleUser, consentCode: Int) {
        (communication as BeurerWeightScale).createUser(user, consentCode)
        updateDataField("")
    }

    fun consent(userIndex: Int, consentCode: Int) {
        (communication as BeurerWeightScale).consentProcedure(userIndex, consentCode)
        updateDataField("")
    }

    fun deleteUserData() {
        (communication as BeurerWeightScale).deleteUserData()
        updateDataField("")
    }

    /*
    * DEVICE CALLBACKS
    * * * */

    override fun onDeviceStateChanged(state: States?) {
        updateDeviceState(state)
    }

    override fun onResults(
        status: MeasurementStatus,
        measurement: ScaleMeasurement?,
        bodyComposition: ScaleBodyComposition?
    ) {
        updateDataField(
            status.toString() + measurement.toString()
                    + bodyComposition.toString()
        )
    }

    override fun onUserList(users: ArrayList<ScaleUser>?) {
        updateDataField(users.toString())
    }

    override fun onUserCreated(index: Int?) {
        updateDataField("New user index: $index")
    }

    override fun onUserDataDeleted(success: Boolean) {
        updateDataField("On Deleted -> " + if (success) "Success" else "Failed")
    }

    override fun onConsent(success: Boolean) {
        updateDataField("On consent -> " + if (success) "Success" else "Failed")
    }

}


