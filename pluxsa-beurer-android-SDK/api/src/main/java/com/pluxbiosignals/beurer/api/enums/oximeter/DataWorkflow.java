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

public enum DataWorkflow {
    START                   ((byte) 0x00),
    CONTINUE_NEXT_GROUP     ((byte) 0x01),
    RESEND_LAST_GROUP       ((byte) 0x02),
    ALL_DATA_RECEIVED_SAVE  ((byte) 0x7E),
    ALL_DATA_RECEIVED_DELETE((byte) 0x7F);

    private final byte value;

    DataWorkflow(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
