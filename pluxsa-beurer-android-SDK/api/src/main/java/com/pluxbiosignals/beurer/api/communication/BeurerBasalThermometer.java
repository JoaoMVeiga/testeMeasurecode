/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.communication;

import android.content.Context;

import com.pluxbiosignals.beurer.api.DeviceScan;

import java.util.UUID;

/**
 * Communication class for handling interactions with Beurer basal thermometer device.
 */
public class BeurerBasalThermometer extends BeurerThermometer {

    /**
     * Communication constructor, sets the application context.
     *
     * @param context The communication context.
     */
    public BeurerBasalThermometer(Context context) {
        super(context);

        //measurement characteristic
        mainServiceUUID = DeviceScan.ktTempBasalServiceUUID;
        chTemperatureMeasurement = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    }
}
