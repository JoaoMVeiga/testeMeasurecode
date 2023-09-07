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

public enum GlucMedsUnit {
   KG,
   L;

   public static GlucMedsUnit getUnit(int value) {
      return value != 0 ? L : KG;
   }
}
