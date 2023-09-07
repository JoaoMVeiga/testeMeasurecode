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

public enum GlucOperatorFilterType {
    NONE            ((byte) 0x00),
    SEQUENCE_NUMBER ((byte) 0x01),
    USER_FACING_TIME((byte) 0x02);

    final byte code;

    GlucOperatorFilterType(byte b) {
        code = b;
    }
}
