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

public enum GlucUnit {
    KG_L,
    MOL_L;

    public static GlucUnit getUnit(int value) {
        return value != 0 ? MOL_L : KG_L;
    }

}
