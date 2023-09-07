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


public enum ReservedFloatValues {
    MDER_S_POSITIVE_INFINITY((byte) 0x007FFFFE),
    MDER_S_NaN((byte) 0x007FFFFF),
    MDER_S_NRes((byte) 0x00800000),
    MDER_S_RESERVED_VALUE((byte) 0x00800001),
    MDER_S_NEGATIVE_INFINITY((byte) 0x00800002);

    private final byte value;

    ReservedFloatValues(byte b) {
        value = b;
    }

    public static ReservedFloatValues getReservedFloatValue(int value) {
        for (ReservedFloatValues reservedValue : ReservedFloatValues.values()) {
            if (reservedValue.value == (byte) (value & 0xFF)) {
                return reservedValue;
            }
        }
        return null;
    }
}
