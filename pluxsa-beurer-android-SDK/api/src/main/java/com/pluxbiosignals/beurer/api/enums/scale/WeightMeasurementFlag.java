package com.pluxbiosignals.beurer.api.enums.scale;

public enum WeightMeasurementFlag {
    UNIT      ((byte) 0x01),   // Bit 0, Weight Units, kg and m -> 0 or lb and in -> 1
    TIMESTAMP ((byte) 0x02),   // Bit 1, Time Stamp, True or False
    USER_ID   ((byte) 0x04),   // Bit 2, User ID, True or False
    BMI_HEIGHT((byte) 0x08);  // Bit 3, BMI and Height, True or False

    private final byte value;

    WeightMeasurementFlag(byte value) {
        this.value = value;
    }
    public byte getValue() {
        return value;
    }
}



