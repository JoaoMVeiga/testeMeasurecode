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

public enum GlucResponseCodes {
    RESPONSE_SUCCESS                ((byte) 0x01),
    RESPONSE_OP_CODE_NOT_SUPPORTED  ((byte) 0x02),
    RESPONSE_INVALID_OPERATOR       ((byte) 0x03),
    RESPONSE_OPERATOR_NOT_SUPPORTED ((byte) 0x04),
    RESPONSE_INVALID_OPERAND        ((byte) 0x05),
    RESPONSE_NO_RECORDS_FOUND       ((byte) 0x06),
    RESPONSE_ABORT_UNSUCCESSFUL     ((byte) 0x07),
    RESPONSE_PROCEDURE_NOT_COMPLETED((byte) 0x08),
    RESPONSE_OPERAND_NOT_SUPPORTED  ((byte) 0x09),
    UNKNOWN                         ((byte) 0xFF);

    private final byte value;

    GlucResponseCodes(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static GlucResponseCodes getResponseCode(int responseCode) {
        for(GlucResponseCodes opCode: GlucResponseCodes.values()){
            if(opCode.value == responseCode){
                return opCode;
            }
        }
        return UNKNOWN;
    }
}
