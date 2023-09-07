/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums.scale;

public enum MeasurementStatus {
    SUCCESS                 ((byte)0x01),
    TAKE_MEASUREMENT_TIMEOUT((byte)0x02),
    TAKE_MEASUREMENT_FAILED ((byte)0x03),
    NO_USER_SELECTED        ((byte)0x04),
    UNKNOWN                 ((byte)0xFF);

    final byte code;

    MeasurementStatus(byte b) {
        code = b;
    }

    public static MeasurementStatus getStatus(byte data) {
        for (MeasurementStatus status : MeasurementStatus.values()) {
            if (status.code == data) {
                return status;
            }
        }

        return UNKNOWN;
    }
}
