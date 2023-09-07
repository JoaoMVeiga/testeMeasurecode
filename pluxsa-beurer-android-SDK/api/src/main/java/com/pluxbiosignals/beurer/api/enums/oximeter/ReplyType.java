/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums.oximeter;

public enum ReplyType {
    VERSION     ((byte) 0xF2),
    SET_TIME    ((byte) 0xF3),
    GET_TIME    ((byte) 0xF9),
    STORAGE_INFO((byte) 0xE0),
    DATA        ((byte) 0xE9),
    UNKNOWN     ((byte) 0xFF);

    final byte header;

    ReplyType(byte b) {
        header = b;
    }


    public static ReplyType getReplyType(byte receivedHeader) {
        for (ReplyType replyType : ReplyType.values()) {
            if (replyType.header == receivedHeader) {
                return replyType;
            }
        }

        return UNKNOWN;
    }

}


