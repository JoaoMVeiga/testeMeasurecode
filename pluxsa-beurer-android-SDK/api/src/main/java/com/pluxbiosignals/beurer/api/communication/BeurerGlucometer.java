package com.pluxbiosignals.beurer.api.communication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

import com.pluxbiosignals.beurer.api.DeviceScan;
import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucOPCodes;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucResponseCodes;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucometerCommands;
import com.pluxbiosignals.beurer.api.interfaces.IGlucCallback;
import com.pluxbiosignals.beurer.api.interfaces.devices.IGlucometer;
import com.pluxbiosignals.beurer.api.objects.GlucContext;
import com.pluxbiosignals.beurer.api.objects.GlucMeasurement;
import com.pluxbiosignals.beurer.api.utils.Logger;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Communication class for handling interactions with Beurer glucometer device.
 */
@SuppressLint("MissingPermission")
public class BeurerGlucometer extends BeurerLECommunication implements IGlucometer {

    //characteristics UUID - indicate, write
    private static final UUID chRecordAccessControlPointUuid = UUID.fromString("00002a52-0000-1000-8000-00805f9b34fb");

    //characteristics UUID - notify only
    private static final UUID chGlucoseMeasurementUuid = UUID.fromString("00002a18-0000-1000-8000-00805f9b34fb");
    private static final UUID chGlucoseMeasurementContextUuid = UUID.fromString("00002a34-0000-1000-8000-00805f9b34fb");


    private BluetoothGattCharacteristic characteristicWrite;

    private final ArrayList<GlucMeasurement> measurements = new ArrayList<>();
    private final ArrayList<GlucContext> contexts = new ArrayList<>();


    /**
     * Communication constructor, sets the application context.
     *
     * @param context The communication context.
     */
    public BeurerGlucometer(Context context) {
        super(context);

        mainServiceUUID = DeviceScan.ktGlucServiceUUID;
    }

    @Override
    protected void onCharacteristicWriteStatus(UUID uuid, int status) {

    }

    @Override
    protected void dealWithReceivedData(UUID uuid, byte[] data) {
        try {
            if (uuid.equals(chGlucoseMeasurementUuid)) {//measurement
                GlucMeasurement measurement = GlucMeasurement.parseReply(data);
                measurements.add(measurement);

            } else if (uuid.equals(chGlucoseMeasurementContextUuid)) {//measurement context
                GlucContext context = GlucContext.parseReply(data);
                contexts.add(context);

            } else { // Record Access Control Point characteristic

                final GlucOPCodes opCode = GlucOPCodes.getOPCode(data[0]);

                switch (opCode) {
                    case NUMBER_OF_STORED_RECORDS_RESPONSE://total records
                        final int number = ((data[1] << 8) | (data[2] & 0xFF));
                        Logger.d(TAG, "# stored records - " + number);

                        if (callback != null) {
                            ((IGlucCallback) callback).onTotalRecords(number);
                        }
                        break;

                    case RESPONSE_CODE:
                        dealWithResponse(data);
                        break;

                    default:
                        Logger.w(TAG, "Unknown OP code");
                        break;
                }
            }

        } catch (Exception e) {
            Logger.e(TAG, "Failed to parse data", e);
        }
    }

    private void dealWithResponse(byte[] data) {
        int offset = 2;

        final GlucOPCodes opCode = GlucOPCodes.getOPCode(data[offset]);
        final GlucResponseCodes responseCode = GlucResponseCodes.getResponseCode(data[offset + 1]);

        Logger.d(TAG, "Requested opcode " + opCode);
        Logger.d(TAG, "GlucResponseCodes " + responseCode);


        switch (responseCode) {
            case RESPONSE_OP_CODE_NOT_SUPPORTED:
            case RESPONSE_INVALID_OPERATOR:
            case RESPONSE_OPERATOR_NOT_SUPPORTED:
            case RESPONSE_INVALID_OPERAND:
            case RESPONSE_OPERAND_NOT_SUPPORTED:
                Logger.w(TAG, "Unsupported request -> " + responseCode);
                break;

            case RESPONSE_SUCCESS:
            case RESPONSE_NO_RECORDS_FOUND:
                if (opCode.equals(GlucOPCodes.REPORT_STORED_RECORDS)) {
                    if (callback != null) {
                        ((IGlucCallback) callback).onResults(measurements, contexts);
                    }
                    contexts.clear();
                    measurements.clear();
                }
                break;

            case RESPONSE_ABORT_UNSUCCESSFUL:
                Logger.w(TAG, "Abort unsuccessful");
                break;

            case RESPONSE_PROCEDURE_NOT_COMPLETED:
                Logger.w(TAG, "Procedure not completed");
                break;

            default:
                break;
        }
    }


    @Override
    protected void checkCharacteristics() {
        final BluetoothGattCharacteristic ctx = deviceMainService
                .getCharacteristic(chGlucoseMeasurementContextUuid);
        setCharacteristicConfigDescriptor(ctx, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        characteristicWrite = deviceMainService.getCharacteristic(chRecordAccessControlPointUuid);
    }

    @Override
    protected void checkDescriptorStatus(BluetoothGattDescriptor descriptor, int status) {
        if (descriptor.getCharacteristic().getUuid().equals(chGlucoseMeasurementContextUuid)) {
            final BluetoothGattCharacteristic measure = deviceMainService
                    .getCharacteristic(chGlucoseMeasurementUuid);
            setCharacteristicConfigDescriptor(measure, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        } else if (descriptor.getCharacteristic().getUuid().equals(chGlucoseMeasurementUuid)) {
            final BluetoothGattCharacteristic access = deviceMainService
                    .getCharacteristic(chRecordAccessControlPointUuid);
            setCharacteristicConfigDescriptor(access, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        }
    }


    @Override
    public void getAllRecords() {
        byte[] command = GlucometerCommands.GET_ALL.getCommand(null);
        writeCharacteristic(characteristicWrite, command);
    }

    @Override
    public void getFirstRecord() {
        byte[] command = GlucometerCommands.GET_FIRST.getCommand(null);
        writeCharacteristic(characteristicWrite, command);
    }

    @Override
    public void getLastRecord() {
        byte[] command = GlucometerCommands.GET_LAST.getCommand(null);
        writeCharacteristic(characteristicWrite, command);
    }

    @Override
    public void getRecordsLessThenOrEqualTo(int highLimit) {
        byte[] command = GlucometerCommands.GET_LESS_THAN.getCommand(new Integer[]{highLimit});
        writeCharacteristic(characteristicWrite, command);
    }

    @Override
    public void getRecordsGreaterThenOrEqualTo(int lowLimit) {
        byte[] command = GlucometerCommands.GET_GREATER_THAN.getCommand(new Integer[]{lowLimit});
        writeCharacteristic(characteristicWrite, command);
    }

    @Override
    public void getStoredRecordsFromRange(int lowLimit, int highLimit) {
        byte[] command = GlucometerCommands.GET_IN_RANGE
                .getCommand(new Integer[]{lowLimit, highLimit});
        writeCharacteristic(characteristicWrite, command);
    }

    @Override
    public void getNumberOfStoredRecords() {
        byte[] command = GlucometerCommands.GET_TOTAL.getCommand(null);
        writeCharacteristic(characteristicWrite, command);
    }

    @Override
    public void abort() {
        byte[] command = GlucometerCommands.ABORT.getCommand(null);
        writeCharacteristic(characteristicWrite, command);
    }

}
