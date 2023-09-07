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

public enum GlucOperatorCodes {
    OPERATOR_NULL                 ((byte) 0x00),
    OPERATOR_ALL_RECORDS          ((byte) 0x01),
    OPERATOR_LESS_THAN_OR_EQUAL   ((byte) 0x02),
    OPERATOR_GREATER_THAN_OR_EQUAL((byte) 0x03),
    OPERATOR_WITHIN_RANGE         ((byte) 0x04),
    OPERATOR_FIRST_RECORD         ((byte) 0x05),
    OPERATOR_LAST_RECORD          ((byte) 0x06),
    UNKNOWN                       ((byte) 0xFF);

    final byte code;

    GlucOperatorCodes(byte b) {
        code = b;
    }

    public static GlucOperatorCodes getOpCode(int requestedOpCode) {
        for(GlucOperatorCodes opCode: GlucOperatorCodes.values()){
            if(opCode.code == requestedOpCode){
                return opCode;
            }
        }
        return UNKNOWN;
    }
}
