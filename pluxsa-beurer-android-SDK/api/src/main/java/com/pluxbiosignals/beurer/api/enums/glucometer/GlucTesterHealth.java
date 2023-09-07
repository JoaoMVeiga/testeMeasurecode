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

public enum GlucTesterHealth {
    FUTURE_USES        ((byte) 0x00),
    MINOR_HEALTH_ISSUES((byte) 0x01),
    MAJOR_HEALTH_ISSUES((byte) 0x02),
    DURING_MENSES      ((byte) 0x03),
    UNDER_STRESS       ((byte) 0x04),
    NO_HEALTH_ISSUES   ((byte) 0x05),
    HEALTH_NO          ((byte) 0x06);

    private final byte value;

    GlucTesterHealth(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static GlucTesterHealth getTesterHealth(byte received) {
        for (GlucTesterHealth testerHealth : GlucTesterHealth.values()) {
            if (testerHealth.value == received) {
                return testerHealth;
            }
        }

        return HEALTH_NO;
    }

}

