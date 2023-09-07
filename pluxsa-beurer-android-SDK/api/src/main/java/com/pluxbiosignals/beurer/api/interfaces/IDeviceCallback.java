package com.pluxbiosignals.beurer.api.interfaces;

import android.os.Parcelable;

import com.pluxbiosignals.beurer.api.States;

public interface IDeviceCallback {
    void onDeviceStateChanged(States state);
}
