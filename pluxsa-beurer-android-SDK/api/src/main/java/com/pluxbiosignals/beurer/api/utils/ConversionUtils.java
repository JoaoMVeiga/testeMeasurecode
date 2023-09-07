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


import com.pluxbiosignals.beurer.api.enums.ReservedFloatValues;
import com.pluxbiosignals.beurer.api.enums.ReservedSFloatValues;

public class ConversionUtils {

    public static float ieeeFloat(byte[] data) {
        int tempData = ((int) data[0] & 0xFF) |
                (((int) data[1] & 0xFF) << 8) |
                (((int) data[2] & 0xFF) << 16) |
                (((int) data[3] & 0xFF) << 24);

        ReservedFloatValues reservedFloatValue = ReservedFloatValues.getReservedFloatValue(tempData);

        if (reservedFloatValue != null) {
            switch (reservedFloatValue) {
                case MDER_S_NaN:
                case MDER_S_NRes:
                case MDER_S_RESERVED_VALUE:
                    return Float.NaN;
                case MDER_S_POSITIVE_INFINITY:
                    return Float.POSITIVE_INFINITY;
                case MDER_S_NEGATIVE_INFINITY:
                    return Float.NEGATIVE_INFINITY;
                default:
                    break;
            }
        }

        byte exponent = (byte) (tempData >> 24);
        int mantissa = tempData & 0x00FFFFFF;

        return (float) (mantissa * Math.pow(10, exponent));
    }

    public static float sFloat(short value) {
        final ReservedSFloatValues reservedSFloatValue = ReservedSFloatValues.fromValue(value);

        if (reservedSFloatValue != null) {
            switch (reservedSFloatValue) {
                case MDER_S_NaN:
                case MDER_S_NRes:
                case MDER_S_RESERVED_VALUE:
                    return Float.NaN;
                case MDER_S_POSITIVE_INFINITY:
                    return Float.POSITIVE_INFINITY;
                case MDER_S_NEGATIVE_INFINITY:
                    return Float.NEGATIVE_INFINITY;
                default:
                    break;
            }
        }

        short exponent;
        if (((value >> 12) & 0x8) != 0) {
            exponent = (short) -((~(value >> 12) & 0x0F) + 1);
        } else {
            exponent = (short) ((value >> 12) & 0x0F);
        }

        short mantissa;
        if ((value & 0x0800) != 0) {
            mantissa = (short) -((~value & 0x0FFF) + 1);
        } else {
            mantissa = (short) (value & 0x0FFF);
        }

        return (float) mantissa * (float) Math.pow(10, exponent);
    }
}