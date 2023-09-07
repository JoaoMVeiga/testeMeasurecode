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

import android.os.Parcelable;

public interface IGenericCallback extends IDeviceCallback {

    void onData(Parcelable data);
}
