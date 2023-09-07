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

public enum GlucLocation {
    FUTURE_USES                  ((byte)  0x00),
    FINGER                       ((byte)  0x01),
    ALTERNATE_SITE_TEST          ((byte)  0x02),
    EARLOBE                      ((byte)  0x03),
    CONTROL_SOLUTION             ((byte)  0x04),
    SAMPLE_LOCATION_NOT_AVAILABLE((byte) 0x14);

    private final byte value;

    GlucLocation(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static GlucLocation getLocation(byte received) {
        for (GlucLocation location : GlucLocation.values()) {
            if (location.value == received) {
                return location;
            }
        }

        return SAMPLE_LOCATION_NOT_AVAILABLE;
    }

}
