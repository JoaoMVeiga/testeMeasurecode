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

public enum TemperatureFlag {

    UNITS    ((byte) 0x01),// Bit 0,Temperature Units Flag, Cº -> 0, F -> 1
    TIMESTAMP((byte) 0x02),// Bit 1, Time Stamp Field, True or False
    TYPE     ((byte) 0x04),// Bit 2, Temperature Type Field, True or False
    FEVER    ((byte) 0x80); // Bit 7, Fever, True or False

    final byte flag;

    TemperatureFlag(byte b) {
        flag = b;
    }

    public byte getFlag() {
        return flag;
    }
}

