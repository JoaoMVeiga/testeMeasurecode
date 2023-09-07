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

public enum GlucOPCodes {
    REPORT_STORED_RECORDS            ((byte) 0x01),
    DELETE_STORED_RECORDS            ((byte) 0x02),
    ABORT_OPERATION                  ((byte) 0x03),
    REPORT_NUMBER_OF_RECORDS         ((byte) 0x04),
    NUMBER_OF_STORED_RECORDS_RESPONSE((byte) 0x05),
    RESPONSE_CODE                    ((byte) 0x06),
    UNKNOWN                          ((byte) 0xFF);

    final byte code;

    GlucOPCodes(byte b) {
        code = b;
    }

    public static GlucOPCodes getOPCode(byte receivedCode) {
        for(GlucOPCodes opCode: GlucOPCodes.values()){
            if(opCode.code == receivedCode){
                return opCode;
            }
        }
        return UNKNOWN;
    }
}
