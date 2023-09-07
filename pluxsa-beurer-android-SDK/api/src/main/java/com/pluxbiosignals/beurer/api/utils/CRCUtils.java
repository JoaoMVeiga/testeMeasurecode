/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.utils;

import java.util.Arrays;

public class CRCUtils {


    public static byte pulseOximeterCRC(byte[] data) {
        int checksum = 0;
        for (byte datum : data) {
            checksum += datum & 0xFF; // Convert to unsigned byte value
        }
        checksum = (checksum & 0x7F);
        return (byte) checksum;
    }

    public static boolean checkPulseOxiChecksum(byte[] data) {
        byte computed = pulseOximeterCRC(Arrays.copyOfRange(data, 0, data.length-1));
        byte received = data[data.length - 1];

        return computed == received;
    }
}
