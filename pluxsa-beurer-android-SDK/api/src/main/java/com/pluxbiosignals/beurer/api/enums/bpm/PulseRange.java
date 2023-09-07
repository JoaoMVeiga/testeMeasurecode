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

public enum PulseRange {
    IN_RANGE           ((byte) 0x00),  // 00 = in range
    EXCEEDS_UPPER_LIMIT((byte) 0x01),  // 01 = exceeds upper limit
    BELOW_LOWER_LIMIT  ((byte) 0x02),  // 10 = below lower limit
    RESERVED           ((byte) 0x03),  // 11 = reserved for future use
    UNKNOWN            ((byte) 0xFF);

    private final byte value;

    PulseRange(byte value) {
        this.value = value;
    }

    public static PulseRange getPulseRange(byte received) {
        for (PulseRange replyType : PulseRange.values()) {
            if (replyType.value == received) {
                return replyType;
            }
        }

        return UNKNOWN;
    }
}
