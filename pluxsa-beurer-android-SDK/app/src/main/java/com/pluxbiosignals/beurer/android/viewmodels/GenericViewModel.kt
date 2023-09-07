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

import android.os.Parcelable
import com.pluxbiosignals.beurer.android.model.Device
import com.pluxbiosignals.beurer.android.viewmodels.base.DeviceViewModel
import com.pluxbiosignals.beurer.api.States
import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication
import com.pluxbiosignals.beurer.api.interfaces.IGenericCallback


class GenericViewModel(communication: BeurerLECommunication, selectedDevice: Device) :
    DeviceViewModel(communication, selectedDevice), IGenericCallback {

    private val TAG = this.javaClass.simpleName

    init {
        communication.init(this)
    }

    /*
    * DEVICE CALLBACKS
    * * */


    override fun onDeviceStateChanged(state: States?) {
        updateDeviceState(state)
    }

    override fun onData(data: Parcelable?) {
        updateDataField(data.toString())
    }
}