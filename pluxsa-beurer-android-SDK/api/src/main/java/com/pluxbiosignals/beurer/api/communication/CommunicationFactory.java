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

import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication;
import com.pluxbiosignals.beurer.api.enums.Devices;

/**
 * Factory class for creating communication instances for different types of devices.
 */
public class CommunicationFactory {

    /**
     * Gets appropriate communication class able to handle all the interactions with device of the
     * provided type.
     *
     * @param deviceType The type of device in use.
     * @param context    The current context.
     * @return An instance of the appropriate communication class, or null if the device type is
     * unknown.
     */
    public static BeurerLECommunication getCommunication(Devices deviceType, Context context) {

        switch (deviceType) {
            case TEMP_BASAL:
                return new BeurerBasalThermometer(context);

            case GLUCOMETER:
                return new BeurerGlucometer(context);

            case OXIMETER:
                return new BeurerOximeter(context);

            case TEMP:
                return new BeurerThermometer(context);

            case BPM:
                return new BeurerBloodPressure(context);

            case SCALE:
                return new BeurerWeightScale(context);

            case UNKNOWN:
                return null;
        }

        return null;
    }
}
