/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums;


public enum ReservedSFloatValues {
    MDER_S_POSITIVE_INFINITY((short) 0x07FE),
    MDER_S_NaN((short) 0x07FF),
    MDER_S_NRes((short) 0x0800),
    MDER_S_RESERVED_VALUE((short) 0x0801),
    MDER_S_NEGATIVE_INFINITY((short) 0x0802);

    private final short value;

    ReservedSFloatValues(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public static ReservedSFloatValues fromValue(short value) {
        for (ReservedSFloatValues enumValue : ReservedSFloatValues.values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
