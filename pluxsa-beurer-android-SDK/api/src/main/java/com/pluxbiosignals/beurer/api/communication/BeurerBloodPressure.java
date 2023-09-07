package com.pluxbiosignals.beurer.api.communication;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

import com.pluxbiosignals.beurer.api.DeviceScan;
import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication;
import com.pluxbiosignals.beurer.api.interfaces.IGenericCallback;
import com.pluxbiosignals.beurer.api.objects.BPMeasurement;
import com.pluxbiosignals.beurer.api.utils.Logger;

import java.util.UUID;

/**
 * Communication class for handling interactions with Beurer blood pressure device.
 */
public class BeurerBloodPressure extends BeurerLECommunication {

    //characteristics UUID
    private static final UUID bpMeasurementCh = UUID.fromString("00002a35-0000-1000-8000-00805f9b34fb");//indicate

    /**
     * Communication constructor, sets the application context.
     *
     * @param context The communication context.
     */
    public BeurerBloodPressure(Context context) {
        super(context);

        mainServiceUUID = DeviceScan.ktBPMonServiceUUID;
    }

    @Override
    protected void onCharacteristicWriteStatus(UUID uuid, int status) {

    }

    @Override
    protected void dealWithReceivedData(UUID uuid, byte[] data) {
        try {
            final BPMeasurement bpm = BPMeasurement.parseReply(data);
            Logger.d(TAG, "BPMeasurement -> " + bpm);

            if (callback != null) {
                ((IGenericCallback) callback).onData(bpm);
            }

        } catch (Exception e) {
            Logger.e(TAG, "Failed parsing", e);
        }
    }

    @Override
    protected void checkCharacteristics() {
        for (BluetoothGattCharacteristic ch : deviceMainService.getCharacteristics()) {
            Logger.d(TAG, " ch " + ch.getUuid());
        }

        final BluetoothGattCharacteristic chMeasure = deviceMainService.getCharacteristic(bpMeasurementCh);
        setCharacteristicConfigDescriptor(chMeasure, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
    }

    @Override
    protected void checkDescriptorStatus(BluetoothGattDescriptor descriptor, int status) {
    }


}
