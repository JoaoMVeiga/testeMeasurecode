package com.pluxbiosignals.beurer.api.enums.scale;

public enum UserListReplyType {
    USER     ((byte) 0x00),
    SUCCESS  ((byte) 0x01),
    NO_USERS ((byte) 0x02),
    UNKNOWN  ((byte) 0xFF);

    final byte code;

    UserListReplyType(byte b) {
        code = b;
    }

    public static UserListReplyType getReplyType(byte data) {
        for (UserListReplyType reply : UserListReplyType.values()) {
            if (reply.code == data) {
                return reply;
            }
        }

        return UNKNOWN;
    }
}
