package com.pluxbiosignals.beurer.api.enums.scale;

public enum ControlPointStatus {
    SUCCESS             ((byte) 0x01),
    NOT_SUPPORTED       ((byte) 0x02),
    OPERATION_FAILED    ((byte) 0x04),
    USER_NOT_AUTHORIZED ((byte) 0x05),
    UNKNOWN             ((byte) 0xFF);

    final byte code;

    ControlPointStatus(byte b) {
        code = b;
    }

    public static ControlPointStatus getControlPointStatus(byte data) {
        for (ControlPointStatus replyType : ControlPointStatus.values()) {
            if (replyType.code == data) {
                return replyType;
            }
        }

        return UNKNOWN;
    }
}
