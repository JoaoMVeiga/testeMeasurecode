package com.pluxbiosignals.beurer.api.communication;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

import com.pluxbiosignals.beurer.api.DeviceScan;
import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication;
import com.pluxbiosignals.beurer.api.interfaces.IGenericCallback;
import com.pluxbiosignals.beurer.api.objects.Temperature;
import com.pluxbiosignals.beurer.api.utils.Logger;

import java.util.UUID;

/**
 * Communication class for handling interactions with Beurer thermometer device.
 */
public class BeurerThermometer extends BeurerLECommunication {

    //indicate
    protected static UUID chTemperatureMeasurement;

    /**
     * Communication constructor, sets the application context.
     *
     * @param context The communication context.
     */
    public BeurerThermometer(Context context) {
        super(context);

        mainServiceUUID = DeviceScan.ktTempServiceUUID;
        chTemperatureMeasurement = UUID.fromString("00002a1c-0000-1000-8000-00805f9b34fb");
    }

    @Override
    protected void onCharacteristicWriteStatus(UUID uuid, int status) {

    }

    @Override
    protected void dealWithReceivedData(UUID uuid, byte[] data) {
        try {
            final Temperature temperature = Temperature.parseReply(data);
            Logger.d(TAG, "Temperature -> " + temperature);

            if (callback != null) {
                ((IGenericCallback) callback).onData(temperature);
            }

        } catch (Exception e) {
            Logger.e(TAG, "Failed parsing", e);
        }
    }

    @Override
    protected void checkCharacteristics() {
        final BluetoothGattCharacteristic chRead = deviceMainService.getCharacteristic(chTemperatureMeasurement);
        setCharacteristicConfigDescriptor(chRead, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
    }

    @Override
    protected void checkDescriptorStatus(BluetoothGattDescriptor descriptor, int status) {
    }


}
