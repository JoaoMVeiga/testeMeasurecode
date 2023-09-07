package com.pluxbiosignals.beurer.api.utils;

public class BitwiseOperations {

    /**
     * Auxiliary class used for getting a hexadecimal format string from an array of bytes.
     *
     * @param bytes array of bytes
     * @return hexadecimal string
     */
    public static String bytesToHexadecimalString(byte[] bytes) {
        if (bytes == null) {
            return "null";
        }
        final StringBuilder str = new StringBuilder();
        for (byte i : bytes) {
            str.append(String.format("%02X ", i));
        }
        return str.toString();
    }
}
