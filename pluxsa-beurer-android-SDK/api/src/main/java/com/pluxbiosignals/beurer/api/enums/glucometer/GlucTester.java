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

public enum GlucTester {
    FUTURE_USES             ((byte) 0x00),
    SELF                    ((byte) 0x01),
    HEALTH_CARE_PROFESSIONAL((byte) 0x02),
    LAB_TEST                ((byte) 0x03),
    TESTER_NOT_AVAILABLE    ((byte) 0x0F);

    private final byte value;

    GlucTester(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static GlucTester getTester(byte received) {
        for (GlucTester tester : GlucTester.values()) {
            if (tester.value == received) {
                return tester;
            }
        }

        return TESTER_NOT_AVAILABLE;
    }

}
