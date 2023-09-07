/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums.thermometer;

public enum TemperatureType {
    RESERVED         ((byte)0x00),
    ARMPIT           ((byte)0x01),
    BODY             ((byte)0x02),
    EAR              ((byte)0x03),
    FINGER           ((byte)0x04),
    GASTROINTESTINAL ((byte)0x05),
    MOUTH            ((byte)0x06),
    RECTUM           ((byte)0x07),
    TOE              ((byte)0x08),
    TYMPANUM         ((byte)0x09);

    final byte value;

    TemperatureType(byte b) {
        value = b;
    }

    public static TemperatureType getReplyType(byte receivedValue) {
        for (TemperatureType replyType : TemperatureType.values()) {
            if (replyType.value == receivedValue) {
                return replyType;
            }
        }

        return RESERVED;
    }
}
