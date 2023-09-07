/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.interfaces;

import com.pluxbiosignals.beurer.api.objects.GlucContext;
import com.pluxbiosignals.beurer.api.objects.GlucMeasurement;

import java.util.ArrayList;

public interface IGlucCallback extends IDeviceCallback {

    void onResults(ArrayList<GlucMeasurement> measurements, ArrayList<GlucContext> contexts);

    void onTotalRecords(int total);
}
