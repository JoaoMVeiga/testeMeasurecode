package com.pluxbiosignals.beurer.api.enums.scale;

public enum ControlPointReplyType {
    NEW_USER_REPLY    ((byte)0x01),
    CONSENT_REPLY     ((byte)0x02),
    DELETE_USER_REPLY ((byte)0x03),
    UNKNOWN           ((byte)0xFF);

    final byte code;

    ControlPointReplyType(byte b) {
        code = b;
    }

    public static ControlPointReplyType getReplyType(byte data) {
        for (ControlPointReplyType replyType : ControlPointReplyType.values()) {
            if (replyType.code == data) {
                return replyType;
            }
        }

        return UNKNOWN;
    }
}
