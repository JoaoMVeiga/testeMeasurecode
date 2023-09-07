/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums.bpm;

public enum BpmFlag {
    UNITS               ((byte) 0x01),  // Bit 0, Blood Pressure Units, mmHg -> 0 or kPa -> 1
    TIMESTAMP           ((byte) 0x02),  // Bit 1, Time Stamp, True or False
    PULSE_RATE          ((byte) 0x04),  // Bit 2, Pulse Rate, True or False
    USER_ID             ((byte) 0x08),  // Bit 3, User ID, True or False
    MEASUREMENT_STATUS  ((byte) 0x10),  // Bit 4, Measurement Status, True or False
    HSD                 ((byte) 0x40);  // Bit 6, HSD Present, True or False
    final byte value;

    BpmFlag(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}

