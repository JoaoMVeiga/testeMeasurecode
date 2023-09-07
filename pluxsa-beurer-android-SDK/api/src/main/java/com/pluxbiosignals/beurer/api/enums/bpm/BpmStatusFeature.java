/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums.bpm;

public enum BpmStatusFeature {
    BODY_MOVEMENT_DETECTION       ((short) 0x001), // Bit 0, Body Movement Detection Flag, No body Movement -> 0 or Body Movement during measurement -> 1
    CUFF_FIT_DETECTION            ((short) 0x002), // Bit 1, Cuff Fit Detection Flag, Cuff fits properly -> 0 or Cuff too loose -> 1
    IRREGULAR_PULSE_DETECTION     ((short) 0x004), // Bit 2, Irregular Pulse Detection Flag, No irregular pulse detected -> 0 or Irregular pulse detected -> 1
    PULSE_RATE_RANGE_DETECTION    ((short) 0x018), // Bit 3:4, Pulse Rate Range Detection Flags, BloodPressureMeasurementStatusFeaturePulseRateRangeDetectionStrings
    MEASUREMENT_POSITION_DETECTION((short) 0x020), // Bit 5, Measurement Position Detection Flag, Proper measurement position -> 0 or Improper measurement position -> 1
    HSD_DETECTION                 ((short) 0x0C0); // Bit 6:7, HSD Detection Flag

    private final short value;

    BpmStatusFeature(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}





