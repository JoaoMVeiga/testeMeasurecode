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

import com.pluxbiosignals.beurer.api.objects.OximeterMeasure;
import com.pluxbiosignals.beurer.api.objects.OximeterStorage;
import com.pluxbiosignals.beurer.api.objects.OximeterTime;
import com.pluxbiosignals.beurer.api.objects.OximeterVersion;

import java.util.ArrayList;

public interface IOximeterCallback extends IDeviceCallback {

    void onVersion(OximeterVersion version);

    void onTime(OximeterTime time);

    void onStorageInfo(OximeterStorage storage);

    void onData(ArrayList<OximeterMeasure> measures);

    void onSetTimeResult(boolean success);

}
