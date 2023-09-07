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

public enum GlucMeal {
    FUTURE_USES ((byte) 0x00),
    PREPRANDIAL ((byte) 0x01),//before meal
    POSTPRANDIAL((byte) 0x02),//after meal
    FASTING     ((byte) 0x03),
    CASUAL      ((byte) 0x04),
    BEDTIME     ((byte) 0x05),
    UNKNOWN     ((byte) 0xFF);

    private final byte value;

    GlucMeal(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static GlucMeal getMeal(byte received) {
        for (GlucMeal meal : GlucMeal.values()) {
            if (meal.value == received) {
                return meal;
            }
        }

        return UNKNOWN;
    }
}


