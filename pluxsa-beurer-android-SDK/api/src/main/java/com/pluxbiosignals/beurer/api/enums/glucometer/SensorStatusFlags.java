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

public enum SensorStatusFlags {
    LOW_BATTERY                 ((short) 0x001), // Bit 0, Device battery low at time of measurement, True or False
    SENSOR_MALFUNCTION          ((short) 0x002), // Bit 1, Sensor malfunction or faulting at time of measurement, True or False
    SAMPLE_SIZE_INSUFFICIENT    ((short) 0x004), // Bit 2, Sample size for blood or control solution insufficient at time of measurement, True or False
    STRIP_INSERTION_ERROR       ((short) 0x008), // Bit 3, Strip insertion error, True or False
    STRIP_TYPE_INCORRECT        ((short) 0x010), // Bit 4, Strip type incorrect for device, True or False
    RESULT_UPPER_BOUND          ((short) 0x020), // Bit 5, Sensor result higher than the device can process, True or False
    RESULT_LOWER_BOUND          ((short) 0x040), // Bit 6, Sensor result lower than the device can process, True or False
    SENSOR_TEMPERATURE_TOO_HIGH ((short) 0x080), // Bit 7, Sensor temperature too high for valid test/result at time of measurement, True or False
    SENSOR_TEMPERATURE_TOO_LOW  ((short) 0x100), // Bit 8, Sensor temperature too low for valid test/result at time of measurement, True or False
    STRIP_PULLED_TOO_SOON       ((short) 0x200), // Bit 9, Sensor read interrupted because strip was pulled too soon at time of measurement, True or False
    GENERAL_FAULT               ((short) 0x400), // Bit 10, General device fault has occurred in the sensor, True or False
    TIME_FAULT                  ((short) 0x800); // Bit 11, Time fault has occurred in the sensor and time may be inaccurate, True or False

    private final short value;

    SensorStatusFlags(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

}
