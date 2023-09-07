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

public enum GlucCarbohydrateId {
    FUTURE_USES((byte) 0x00),
    BREAKFAST  ((byte) 0x01),
    LUNCH      ((byte) 0x02),
    DINNER     ((byte) 0x03),
    SNACK      ((byte) 0x04),
    DRINK      ((byte) 0x05),
    SUPPER     ((byte) 0x06),
    BRUNCH     ((byte) 0x07),
    UNKNOWN    ((byte) 0xFF);

    private final byte value;

    GlucCarbohydrateId(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static GlucCarbohydrateId getCarbohydrateId(byte received) {
        for (GlucCarbohydrateId carbohydrateId : GlucCarbohydrateId.values()) {
            if (carbohydrateId.value == received) {
                return carbohydrateId;
            }
        }

        return UNKNOWN;
    }

}
