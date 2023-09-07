/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums.thermometer;

public enum TemperatureUnit {
    CELSIUS,
    FAHRENHEIT;

    public static TemperatureUnit getUnit(int value) {
        return value != 0 ? FAHRENHEIT : CELSIUS;
    }
}
