package com.pluxbiosignals.beurer.api;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;

import com.pluxbiosignals.beurer.api.enums.Devices;
import com.pluxbiosignals.beurer.api.interfaces.IDeviceFound;
import com.pluxbiosignals.beurer.api.utils.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("MissingPermission")
public class DeviceScan extends ScanCallback {

    public static final String ktGlucServiceUUID = "00001808-0000-1000-8000-00805f9b34fb";
    public static final String ktTempBasalServiceUUID = "0000FFF0-0000-1000-8000-00805f9b34fb";
    public static final String ktTempServiceUUID = "00001809-0000-1000-8000-00805f9b34fb";
    public static final String ktBPMonServiceUUID = "00001810-0000-1000-8000-00805f9b34fb";
    public static final String ktOxiServiceUUID = "0000ff12-0000-1000-8000-00805f9b34fb";
    public static final String ktScaleServiceUUID = "0000181d-0000-1000-8000-00805f9b34fb";

    private final String TAG = this.getClass().getSimpleName();

    private final BluetoothAdapter adapter;

    private IDeviceFound callback;

    /**
     * BTHDeviceScan constructor
     */
    public DeviceScan() throws Exception {

        //get the default bluetooth adapter
        adapter = BluetoothAdapter.getDefaultAdapter();

        //if the bluetooth adapter is not support, return
        if (adapter == null) {
            throw new Exception();
        }
    }


    /**
     * Stops the bluetooth device discovery
     */
    public void stopScan() {
        //make sure that the adapter is available
        if (adapter == null) {
            return;
        }

        //make sure we're not doing discovery anymore
        adapter.getBluetoothLeScanner().stopScan(this);
    }


    /**
     * Starts the bluetooth device discovery
     */

    public void startScan() {
        //make sure that the adapter is available
        if (adapter == null) {
            return;
        }

        // If we're already discovering, STOP it
        if (adapter.isDiscovering()) {
            stopScan();
        }

        final List<ScanFilter> filters = getFilters();

        final ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
                .setReportDelay(0L)
                .build();

        // Request discover from BluetoothAdapter
        adapter.getBluetoothLeScanner().startScan(filters, scanSettings, this);
    }

    /**
     * Gets list of Beurer services filter.
     *
     * @return Beurer services filter
     */
    private List<ScanFilter> getFilters() {
        final List<ScanFilter> filters = new ArrayList<>();

        final ScanFilter gluFilter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(ktGlucServiceUUID)).build();
        final ScanFilter tempFilter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(ktTempServiceUUID)).build();
        final ScanFilter bpmFilter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(ktBPMonServiceUUID)).build();
        final ScanFilter oxiFilter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(ktOxiServiceUUID)).build();
        final ScanFilter scaleFilter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(ktScaleServiceUUID)).build();
        final ScanFilter basalTemp = new ScanFilter.Builder().setDeviceName("OT30").build();

        filters.add(gluFilter);
        filters.add(tempFilter);
        filters.add(bpmFilter);
        filters.add(oxiFilter);
        filters.add(scaleFilter);
        filters.add(basalTemp);

        return filters;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);

        if (callback != null && result.getScanRecord() != null) {

            final Devices type = getDeviceType(result.getScanRecord().getDeviceName(),
                    result.getScanRecord().getServiceUuids());

            Logger.d(TAG, "Found " + result.getDevice().getAddress() + " - " + type);

            if (type != null) {
                callback.onDeviceFound(result.getDevice(), type);
            }
        }
    }

    private Devices getDeviceType(String name, List<ParcelUuid> serviceUuids) {
        if (name.equals("OT30")) {
            return Devices.TEMP_BASAL;
        }
        if (serviceUuids == null) {
            return null;
        }
        if (serviceUuids.contains(ParcelUuid.fromString(ktGlucServiceUUID))) {
            return Devices.GLUCOMETER;
        } else if (serviceUuids.contains(ParcelUuid.fromString(ktTempServiceUUID))) {
            return Devices.TEMP;
        } else if (serviceUuids.contains(ParcelUuid.fromString(ktBPMonServiceUUID))) {
            return Devices.BPM;
        } else if (serviceUuids.contains(ParcelUuid.fromString(ktOxiServiceUUID))) {
            return Devices.OXIMETER;
        } else if (serviceUuids.contains(ParcelUuid.fromString(ktScaleServiceUUID))) {
            return Devices.SCALE;
        }

        return null;
    }

    public void setCallback(@NotNull IDeviceFound iDeviceFound) {
        callback = iDeviceFound;
    }
}
