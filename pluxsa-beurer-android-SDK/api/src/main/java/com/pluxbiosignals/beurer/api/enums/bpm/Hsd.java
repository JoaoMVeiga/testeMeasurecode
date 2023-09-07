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

public enum Hsd {
    NOT_DETECTED   ((byte) 0x00),  // 00 = HSD not detected
    DETECTED       ((byte) 0x01),  // 01 = HSD detected
    UNABLE_TO_JUDGE((byte) 0x02),  // 10 = unable to judge
    UNKNOWN        ((byte) 0xFF);

    private final byte value;

    Hsd(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static Hsd getHsd(byte received) {
        for (Hsd hsd : Hsd.values()) {
            if (hsd.value == received) {
                return hsd;
            }
        }

        return UNKNOWN;
    }
}
