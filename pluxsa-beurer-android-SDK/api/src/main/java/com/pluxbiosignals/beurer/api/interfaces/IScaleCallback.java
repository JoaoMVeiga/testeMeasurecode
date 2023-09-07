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

import com.pluxbiosignals.beurer.api.enums.scale.MeasurementStatus;
import com.pluxbiosignals.beurer.api.objects.ScaleBodyComposition;
import com.pluxbiosignals.beurer.api.objects.ScaleMeasurement;
import com.pluxbiosignals.beurer.api.objects.ScaleUser;

import java.util.ArrayList;

public interface IScaleCallback extends IDeviceCallback {

    void onResults(MeasurementStatus status, ScaleMeasurement measurements, ScaleBodyComposition bodyCompositions);

    void onUserList(ArrayList<ScaleUser> users);

    void onUserCreated(Integer index);

    void onUserDataDeleted(boolean success);

    void onConsent(boolean success);
}
