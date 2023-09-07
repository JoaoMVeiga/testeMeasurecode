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

public enum GlucMedId {
    FUTURE_USES         ((byte) 0x00),
    RAPID_INSULIN       ((byte) 0x01),
    SHORT_INSULIN       ((byte) 0x02),
    INTERMEDIATE_INSULIN((byte) 0x03),
    LONG_ACTING_INSULIN ((byte) 0x04),
    PRE_MIXED_INSULIN   ((byte) 0x05),
    UNKNOWN             ((byte) 0xFF);

    private final byte value;

    GlucMedId(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static GlucMedId getMedId(byte received) {
        for (GlucMedId med : GlucMedId.values()) {
            if (med.value == received) {
                return med;
            }
        }

        return UNKNOWN;
    }
}
