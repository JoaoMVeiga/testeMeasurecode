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

public enum WeightScaleOPCodes {
    CREATE_USER       ((byte) 0x01),
    CONSENT_PROCEDURE ((byte) 0x02),
    DELETE_USER_DATA  ((byte) 0x03);


    final byte code;

    WeightScaleOPCodes(byte b) {
        code = b;
    }
}
