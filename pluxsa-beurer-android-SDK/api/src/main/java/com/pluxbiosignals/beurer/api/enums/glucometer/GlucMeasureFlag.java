/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums.glucometer;

public enum GlucMeasureFlag {
    TIME_OFFSET                ((byte) 0x01), // Bit 0, Time Offset, True or False
    GLUCOSE_CONCENTRATION      ((byte) 0x02), // Bit 1, Glucose Concentration Field, True or False
    UNIT                       ((byte) 0x04), // Bit 2, Glucose Concentration Units, kg/L or mol/L
    SENSOR_STATUS_ANNUNCIATION ((byte) 0x08), // Bit 3, Sensor Status Annunciation, True or False
    CONTEXT_INFORMATION_FOLLOW ((byte) 0x10); // Bit 4, Context Information Follow, True or False

    private final byte value;

    GlucMeasureFlag(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }


}
