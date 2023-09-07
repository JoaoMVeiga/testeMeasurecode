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

public enum GlucType {
    FUTURE_USES              ((byte) 0x00),
    CAPILLARY_WHOLE_BLOOD    ((byte) 0x01),
    CAPILLARY_PLASMA         ((byte) 0x02),
    VENOUS_WHOLE_BLOOD       ((byte) 0x03),
    VENOUS_PLASMA            ((byte) 0x04),
    ARTERIAL_WHOLE_BLOOD     ((byte) 0x05),
    ARTERIAL_PLASMA          ((byte) 0x06),
    UNDETERMINED_WHOLE_BLOOD ((byte) 0x07),
    UNDETERMINED_PLASMA      ((byte) 0x08),
    INTERSTITIAL_FLUID       ((byte) 0x09),
    CONTROL_SOLUTION         ((byte) 0x0A),
    UNKNOWN                  ((byte) 0xFF);

    private final byte value;

    GlucType(byte value) {
        this.value = value;
    }

    public static GlucType getType(byte received) {
        for (GlucType type : GlucType.values()) {
            if (type.value == received) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
