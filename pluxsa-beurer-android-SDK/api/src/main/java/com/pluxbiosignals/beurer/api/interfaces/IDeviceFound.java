/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.interfaces;

import android.bluetooth.BluetoothDevice;

import com.pluxbiosignals.beurer.api.enums.Devices;

public interface IDeviceFound {

    void onDeviceFound(BluetoothDevice bluetoothDevice, Devices device);
}
