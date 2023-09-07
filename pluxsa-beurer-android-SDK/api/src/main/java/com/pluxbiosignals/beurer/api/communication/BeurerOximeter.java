package com.pluxbiosignals.beurer.api.communication;

import static com.pluxbiosignals.beurer.api.utils.CRCUtils.checkPulseOxiChecksum;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

import com.pluxbiosignals.beurer.api.DeviceScan;
import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication;
import com.pluxbiosignals.beurer.api.enums.oximeter.DataWorkflow;
import com.pluxbiosignals.beurer.api.enums.oximeter.OximeterCommands;
import com.pluxbiosignals.beurer.api.enums.oximeter.ReplyType;
import com.pluxbiosignals.beurer.api.interfaces.IOximeterCallback;
import com.pluxbiosignals.beurer.api.interfaces.devices.IOximeter;
import com.pluxbiosignals.beurer.api.objects.OximeterMeasure;
import com.pluxbiosignals.beurer.api.objects.OximeterStorage;
import com.pluxbiosignals.beurer.api.objects.OximeterTime;
import com.pluxbiosignals.beurer.api.objects.OximeterVersion;
import com.pluxbiosignals.beurer.api.utils.BitwiseOperations;
import com.pluxbiosignals.beurer.api.utils.Logger;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Communication class for handling interactions with Beurer oximeter device.
 */
@SuppressLint("MissingPermission")
public class BeurerOximeter extends BeurerLECommunication implements IOximeter {

    //characteristics UUID
    private static final UUID writeCharacteristic = UUID.fromString("0000ff01-0000-1000-8000-00805f9b34fb");
    private static final UUID readCharacteristic = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic characteristicWrite;

    //Data incomplete
    private boolean needsTotal = false;
    private boolean deleteAfter = false;
    private int currentRequest = 0;
    private int totalData = 0;
    private byte[] incompleteDataArray;
    private final ArrayList<OximeterMeasure> oximeterMeasures = new ArrayList<>();

    /**
     * Communication constructor, sets the application context.
     *
     * @param context The communication context.
     */
    public BeurerOximeter(Context context) {
        super(context);

        mainServiceUUID = DeviceScan.ktOxiServiceUUID;
    }

    @Override
    protected void onCharacteristicWriteStatus(UUID uuid, int status) {

    }

    @Override
    protected void dealWithReceivedData(UUID uuid, byte[] data) {
        final ReplyType type = ReplyType.getReplyType(data[0]);

        Logger.d(TAG, "Received " + type
                + " " + BitwiseOperations.bytesToHexadecimalString(data));

        try {
            switch (type) {
                case VERSION:
                    OximeterVersion version = OximeterVersion.parseReply(data);
                    Logger.d(TAG, "Version -> " + version);

                    if (callback != null) {
                        ((IOximeterCallback) callback).onVersion(version);
                    }

                    break;

                case SET_TIME:
                    boolean crcOk = checkPulseOxiChecksum(data);
                    if (!crcOk) {
                        throw new Exception("Checksum not OK.");
                    }
                    boolean success = data[1] == 0;
                    Logger.d(TAG, "Set time -> " + (success ? "Success" : "Failed"));

                    if (callback != null) {
                        ((IOximeterCallback) callback).onSetTimeResult(success);
                    }
                    break;

                case GET_TIME:
                    OximeterTime time = OximeterTime.parseReply(data);
                    Logger.d(TAG, "Time -> " + time);

                    if (callback != null) {
                        ((IOximeterCallback) callback).onTime(time);
                    }
                    break;

                case STORAGE_INFO:
                    OximeterStorage storage = OximeterStorage.parseReply(data);

                    if (needsTotal) {
                        totalData = storage.getTotalCapacity();

                        if (totalData > 0) {
                            startMeasurementDataWorkflow();

                        } else {
                            if (callback != null) {
                                ((IOximeterCallback) callback).onData(oximeterMeasures);
                            }
                        }
                        needsTotal = false;

                    } else {
                        Logger.d(TAG, "Storage -> " + storage);

                        if (callback != null) {
                            ((IOximeterCallback) callback).onStorageInfo(storage);
                        }
                    }
                    break;

                case DATA:
                case UNKNOWN:
                    dealWithMeasure(data);
                    break;
            }
        } catch (Exception e) {
            Logger.e(TAG, "Failed parsing", e);
        }
    }

    private void dealWithMeasure(byte[] data) throws Exception {
        if (incompleteDataArray == null) {//if empty, add all data that was received
            incompleteDataArray = data;
            return;
        }

        if (incompleteDataArray.length == 0) {
            return;
        }

        byte[] existingData = incompleteDataArray;
        int missingLength = 24 - existingData.length;//to be completed with received data
        int newDataLength = Math.min(missingLength, data.length);
        int remainingLength = data.length - newDataLength;//data that will be incomplete

        byte[] combinedData = new byte[existingData.length + newDataLength];
        System.arraycopy(existingData, 0, combinedData, 0, existingData.length);
        System.arraycopy(data, 0, combinedData, existingData.length, newDataLength);

        if (combinedData.length == 24) {
            Logger.d(TAG, "Received measurement: "
                    + BitwiseOperations.bytesToHexadecimalString(combinedData));

            OximeterMeasure measure = OximeterMeasure.parseReply(combinedData);
            Logger.d(TAG, "Measure -> " + measure);
            oximeterMeasures.add(measure);
            currentRequest++;

            //Update incomplete array if needed
            byte[] remainingData;
            if (remainingLength > 0) {
                remainingData = new byte[data.length - newDataLength];
                System.arraycopy(data, newDataLength, remainingData, 0, remainingData.length);
                incompleteDataArray = remainingData;
            } else {
                incompleteDataArray = null;
            }

            if (currentRequest == 10 && totalData > 10) {
                //request more data
                currentRequest = 0;
                getMeasurementDataNext();
            }

            if (oximeterMeasures.size() == totalData) {
                //received all. no more data to be requested
                Logger.v(TAG, "All data received");

                if (callback != null) {
                    ((IOximeterCallback) callback).onData(oximeterMeasures);
                }

                if (deleteAfter) {
                    getMeasurementAllDataReceivedDelete();
                    deleteAfter = false;
                } else {
                    getMeasurementAllDataReceivedSave();
                }

                currentRequest = 0;
                oximeterMeasures.clear();
            }
        }

    }

    @Override
    protected void checkCharacteristics() {
        final BluetoothGattCharacteristic chRead = deviceMainService.getCharacteristic(readCharacteristic);
        setCharacteristicConfigDescriptor(chRead, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        characteristicWrite = deviceMainService.getCharacteristic(writeCharacteristic);
    }

    @Override
    protected void checkDescriptorStatus(BluetoothGattDescriptor descriptor, int status) {
    }


    /**
     * Command for requesting the device version.
     */
    @Override
    public void getVersion() {
        byte[] command = OximeterCommands.GET_VERSION.getCommand(null);
        writeCharacteristic(characteristicWrite, command);
    }

    /**
     * Command to update the date and time information.
     */
    @Override
    public void setTimeSynchronization() {
        byte[] command = OximeterCommands.SET_TIME_SYNCHRONIZATION.getCommand(null);
        writeCharacteristic(characteristicWrite, command);
    }

    /**
     * Command to request the current time information.
     */
    @Override
    public void getDeviceTime() {
        byte[] command = OximeterCommands.GET_TIME.getCommand(null);
        writeCharacteristic(characteristicWrite, command);
    }

    /**
     * Command to request information about the stored data.
     */
    @Override
    public void getDataStorageInformation() {
        byte[] command = OximeterCommands.GET_DATA_STORAGE_INFO.getCommand(null);
        writeCharacteristic(characteristicWrite, command);
    }

    /**
     * The command to request the stored measurement data.
     *
     * @param deleteAfter true if data is to be deleted after being received, false otherwise.
     */
    @Override
    public void getMeasurementData(boolean deleteAfter) {
        needsTotal = true;
        this.deleteAfter = deleteAfter;

        //check how many measures are available
        getDataStorageInformation();
    }

    private void startMeasurementDataWorkflow() {
        byte[] command = OximeterCommands.GET_MEASUREMENT_DATA
                .getCommand(new Byte[]{DataWorkflow.START.getValue()});
        writeCharacteristic(characteristicWrite, command);
    }


    private void getMeasurementDataNext() {
        byte[] command = OximeterCommands.GET_MEASUREMENT_DATA
                .getCommand(new Byte[]{DataWorkflow.CONTINUE_NEXT_GROUP.getValue()});
        writeCharacteristic(characteristicWrite, command);
    }


    private void getMeasurementResendLastGroup() {
        byte[] command = OximeterCommands.GET_MEASUREMENT_DATA
                .getCommand(new Byte[]{DataWorkflow.RESEND_LAST_GROUP.getValue()});
        writeCharacteristic(characteristicWrite, command);
    }


    private void getMeasurementAllDataReceivedSave() {
        byte[] command = OximeterCommands.GET_MEASUREMENT_DATA
                .getCommand(new Byte[]{DataWorkflow.ALL_DATA_RECEIVED_SAVE.getValue()});
        writeCharacteristic(characteristicWrite, command);
    }


    private void getMeasurementAllDataReceivedDelete() {
        byte[] command = OximeterCommands.GET_MEASUREMENT_DATA
                .getCommand(new Byte[]{DataWorkflow.ALL_DATA_RECEIVED_DELETE.getValue()});
        writeCharacteristic(characteristicWrite, command);
    }
}
