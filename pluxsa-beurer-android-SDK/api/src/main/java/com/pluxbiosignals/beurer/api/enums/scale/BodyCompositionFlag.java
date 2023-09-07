package com.pluxbiosignals.beurer.api.enums.scale;

public enum BodyCompositionFlag {
    UNIT      ((short) 0x01),        // Bit 0, Weight Units, kg and m -> 0 or lb and in -> 1
    TIMESTAMP((short) 0x02),               // Bit 1, Time Stamp, True or False
    USER_ID((short) 0x04),                  // Bit 2, User ID, True or False
    BASAL_METABOLISM((short) 0x08),         // Bit 3, Basal Metabolism, True or False
    MUSCLE_PERCENTAGE((short) 0x10),        // Bit 4, Muscle Percentage, True or False
    MUSCLE_MASS((short) 0x20),              // Bit 5, Muscle Mass, True or False
    FAT_FREE_MASS((short) 0x40),            // Bit 6, Fat Free Mass, True or False
    SOFT_LEAN_MASS((short) 0x80),           // Bit 7, Soft Lean Mass, True or False
    BODY_WATER_MASS((short) 0x100),        // Bit 8, Body Water Mass, True or False
    IMPEDANCE((short) 0x200),               // Bit 9, Impedance, True or False
    WEIGHT((short) 0x400),                  // Bit 10, Weight, True or False
    HEIGHT((short) 0x800),                  // Bit 11, Height, True or False
    MULTI_PACKET((short) 0x1000);           // Bit 12, Multiple Packet, True or False
    private final short value;

    BodyCompositionFlag(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
