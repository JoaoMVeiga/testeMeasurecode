/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums.scale;

public enum ScaleUnit {
    SI, //SI(Weight and Mass in units of kilogram(kg) and Height in units of meter
    IMPERIAL; //Weight and Mass in units of pound (lb) and Height in units of inch (in)


    public static ScaleUnit getUnit(int value) {
        return value != 0 ? IMPERIAL : SI;
    }

}
