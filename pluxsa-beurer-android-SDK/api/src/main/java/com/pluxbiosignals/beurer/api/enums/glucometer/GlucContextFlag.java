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

public enum GlucContextFlag {
    CARBON_HYDRATES ((byte) 0x01), // Bit 0, Carbon Hydrates ID Field and Carbon Hydrates, True or False
    MEAL            ((byte) 0x02), // Bit 1, Meal, True or False
    TESTER_HEALTH   ((byte) 0x04), // Bit 2, Tester-Health, True or False
    EXERCISE        ((byte) 0x08), // Bit 3, Exercise Duration and Intensity, True or False
    MEDICATION_ID   ((byte) 0x10), // Bit 4, Medication ID Field, True or False
    MEDICATION_UNITS((byte) 0x20), // Bit 5, Medication Unit Field, kg -> 0 or L -> 1
    HbA1c           ((byte) 0x40), // Bit 6, HbA1c, True or False
    EXTENDED        ((byte) 0x80); // Bit 7, Extended Flag, True or False

    private final byte value;

    GlucContextFlag(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}

