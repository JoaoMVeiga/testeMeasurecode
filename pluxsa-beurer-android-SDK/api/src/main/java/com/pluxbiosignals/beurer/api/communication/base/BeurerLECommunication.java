package com.pluxbiosignals.beurer.api.communication.base;

import static com.pluxbiosignals.beurer.api.utils.BitwiseOperations.bytesToHexadecimalString;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.pluxbiosignals.beurer.api.States;
import com.pluxbiosignals.beurer.api.communication.BeurerBloodPressure;
import com.pluxbiosignals.beurer.api.communication.BeurerGlucometer;
import com.pluxbiosignals.beurer.api.interfaces.IDeviceCallback;
import com.pluxbiosignals.beurer.api.utils.BitwiseOperations;
import com.pluxbiosignals.beurer.api.utils.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Abstract base class for implementing communication with Beurer Bluetooth Low Energy (BLE) devices.
 */
@SuppressLint("MissingPermission")
public abstract class BeurerLECommunication {

    protected final String TAG = this.getClass().getSimpleName();

    private static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    private final Context context;

    //Communication callbacks
    protected IDeviceCallback callback;

    /*
     * BLE VARIABLES
     */
    private boolean isIncomingPairRequestReceiverRegister = false;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    protected BluetoothGatt bluetoothGatt;
    protected BluetoothGattService deviceMainService;

    /*
     * DEVICE VARIABLES
     */

    protected String mainServiceUUID;
    private String bluetoothDeviceAddress = "00:00:00:00:00:00";

    /*
     * CONNECTION VARIABLES
     */
    private States currentState = States.NO_CONNECTION;

    private final BroadcastReceiver incomingPairRequestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            //if receiving a pairing request
            if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device == null || !device.getAddress().equals(bluetoothDeviceAddress)) {
                    return;
                }

                Logger.d(TAG, "Receiving a PAIRING REQUEST " + intent.getExtras());
                try {
                    //the pin in case you need to accept for an specific pin
                    Logger.d(TAG, "PAIRING_KEY: "
                            + intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_KEY,
                            -1));

                    final int variant = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.ERROR);
                    Logger.d(TAG, "pairing request received: " + " (" + variant + ")");

                    if (BeurerLECommunication.this instanceof BeurerGlucometer) {
                        device.setPin("982249".getBytes());
                    }

                } catch (Exception e) {
                    Logger.e(TAG, "Pairing failed", e);
                }
            }
        }
    };

    /**
     * Communication constructor, sets application context
     *
     * @param context communication context
     */
    public BeurerLECommunication(Context context) {
        this.context = context;
    }

    /**
     * Initializes the communication module.
     *
     * @param deviceCallback The callback interface for device-related events.
     * @throws Exception If Bluetooth manager or adapter are not available.
     */
    public void init(IDeviceCallback deviceCallback) throws Exception {
        this.callback = deviceCallback;

        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                throw new Exception("Bluetooth manager not available");
            }
        }

        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            throw new Exception("Unable to obtain a BluetoothAdapter");
        }

        //register pairing request intent receiver
        if (!isIncomingPairRequestReceiverRegister) {
            final IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
            intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
            context.registerReceiver(incomingPairRequestReceiver, intentFilter);
            isIncomingPairRequestReceiverRegister = true;
        }
    }


    /**
     * Connects to the specified Bluetooth device.
     *
     * @param address The MAC address of the target Bluetooth device.
     * @throws Exception If connection fails or if the MAC address is invalid.
     */
    public void connect(String address) throws Exception {
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            throw new Exception("Error invalid MAC Address");
        }

        bluetoothDeviceAddress = address;

        if (isConnected()) {
            Log.e("Teste ", "Already connected");
            throw new Exception("Already connected");
        }

        if (bluetoothAdapter == null || address == null) {
            Logger.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            throw new Exception();
        }

        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Logger.w(TAG, bluetoothDeviceAddress + " - Device not found.  Unable to connect.");
            throw new Exception("Device not found");
        }

        bluetoothGatt = device.connectGatt(context, false, gattCallback,
                BluetoothDevice.TRANSPORT_LE);
        Logger.d(TAG, bluetoothDeviceAddress + " - Trying to create a new connection.");

        setState(States.CONNECTING);
    }

    /**
     * Disconnects from the connected Bluetooth device.
     *
     * @throws Exception If disconnection fails or if not connected.
     */
    public void disconnect() throws Exception {
        if (!isConnected() || bluetoothGatt == null) {
            throw new Exception("ERROR_DEVICE_NOT_CONNECTED");
        }

        bluetoothGatt.disconnect();
    }

    /**
     * Cleans up resources and releases connections.
     */
    public void finish() {
        Logger.v(TAG, "Finish communication");

        if (bluetoothGatt == null) {
            return;
        }

        bluetoothGatt.close();
        bluetoothGatt = null;

        setState(States.DISCONNECTED);
        Logger.d(TAG, bluetoothDeviceAddress + " - Close mBluetoothGatt.");

        if (isIncomingPairRequestReceiverRegister) {
            context.unregisterReceiver(incomingPairRequestReceiver);
            isIncomingPairRequestReceiverRegister = false;
        }
    }


    /**
     * Resets states when connection is lost
     */
    private void connectionLost() {
        Logger.e(TAG, "connectionLost");

        // Send a failure message back to the Activity
        setState(States.DISCONNECTED);

        if (bluetoothGatt == null) {
            return;
        }

        Logger.i(TAG, bluetoothDeviceAddress + " - Disconnected from GATT server.");
        bluetoothGatt.close();
        bluetoothGatt = null;
    }

    /**
     * Return the current connection state.
     */
    private synchronized States getState() {
        return currentState;
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state an integer defining the current connection state
     */
    private synchronized void setState(States state) {
        currentState = state;

        Logger.d(TAG, "Update device state " + state);

        if (callback != null) {
            callback.onDeviceStateChanged(state);
        }
    }

    /**
     * @return true if device is connected, false otherwise
     */
    private boolean isConnected() {
        Logger.d(TAG, "Current state " + currentState);

        return !currentState.equals(States.DISCONNECTED)
                && !currentState.equals(States.NO_CONNECTION);
    }

    private final Handler request = new Handler();


    /**
     * Implements callback methods for GATT events that the app cares about.
     * For example, connection changes and services discovery.
     */
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Logger.d(TAG, "onConnectionStateChange - status: " + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Logger.d(TAG, "onConnectionStateChange - state: " + newState);

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Logger.i(TAG, bluetoothDeviceAddress + " - Connected to GATT server.");
                    setState(States.CONNECTED);

                    //discover services
                    boolean flag = bluetoothGatt.discoverServices();
                    Logger.i(TAG, bluetoothDeviceAddress
                            + " - Attempting to start service discovery: " + flag);

                    //do not request bond if using BM 57
                    if (!(BeurerLECommunication.this instanceof BeurerBloodPressure)) {
                        gatt.getDevice().createBond();
                    }

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    connectionLost();
                }

            } else {
                connectionLost();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            Logger.d(TAG, bluetoothDeviceAddress + " - onServicesDiscovered received: " + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Logger.d(TAG, "Request high priority!");
                bluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);

                request.postDelayed(() -> {
                    //check discovered services
                    if (bluetoothGatt != null) {
                        checkDiscoveredServices(bluetoothGatt.getServices());
                    }
                }, 500);


            } else {
                finishWithWarning();

                Logger.w(TAG, "Error discovering services");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {

            final byte[] data = characteristic.getValue();//get characteristic value

            if (data == null || data.length == 0) {
                return;
            }
            Logger.d(TAG, "Received from " + characteristic.getUuid() + " -> "
                    + bytesToHexadecimalString(data));

            dealWithReceivedData(characteristic.getUuid(), data);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Logger.i(TAG, bluetoothDeviceAddress + " - characteristic write success- "
                        + characteristic.getUuid());
                onCharacteristicWriteStatus(characteristic.getUuid(), status);

            } else {
                Logger.w(TAG, "Failed write. Ch: " + characteristic.getUuid()
                        + " - status: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(@NonNull BluetoothGatt gatt,
                                         @NonNull BluetoothGattCharacteristic characteristic,
                                         @NonNull byte[] value, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Logger.i(TAG, bluetoothDeviceAddress + " - characteristic read success");
                dealWithReceivedData(characteristic.getUuid(), value);

            } else {
                Logger.w(TAG, "Failed read. Ch: " + characteristic.getUuid()
                        + " - status: " + status);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            Logger.i(TAG, bluetoothDeviceAddress + " - onDescriptorWrite " + " - "
                    + descriptor.getUuid() + " status: " + status);

            checkDescriptorStatus(descriptor, status);
        }

        public void onConnectionUpdated(@NotNull final BluetoothGatt gatt, final int interval,
                                        final int latency, final int timeout, final int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                String msg = String.format(Locale.ENGLISH,
                        "connection parameters: interval=%.1fms latency=%d timeout=%ds",
                        interval * 1.25f, latency, timeout / 100);
                Logger.d(TAG, msg);
            }
        }
    };

    /**
     * Checks the type of characteristic received and proceeds accordingly.
     *
     * @param uuid   The UUID of the received characteristic.
     * @param status Write status.
     */
    protected abstract void onCharacteristicWriteStatus(UUID uuid, int status);

    /**
     * Checks the type of characteristic received and processes the data accordingly.
     *
     * @param uuid The UUID of the received characteristic.
     * @param data The data received from the characteristic.
     */
    protected abstract void dealWithReceivedData(UUID uuid, byte[] data);

    /**
     * Loops through the GATT Services supported by the discovered device.
     *
     * @param gattServices The list of supported GATT services.
     */
    protected void checkDiscoveredServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            finishWithWarning();
            return;
        }

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            Logger.d(TAG, "Gatt service: " + gattService.getUuid().toString());

            // Loops through available Characteristics- check if at least serviceUUID is found
            if (gattService.getUuid().equals(UUID.fromString(mainServiceUUID))) {

                deviceMainService = gattService;
                Logger.v(TAG, "Device Service Found");

                for (BluetoothGattCharacteristic ch : deviceMainService.getCharacteristics()) {
                    Logger.d(TAG, " ch " + ch.getUuid());
                }

                checkCharacteristics();
                break;
            }
        }
    }

    /**
     * Enables or disables notification on a given characteristic.
     *
     * @param characteristic The characteristic to act on.
     * @param type           The notification type.
     */
    protected void setCharacteristicConfigDescriptor(BluetoothGattCharacteristic characteristic,
                                                     byte[] type) {
        if (bluetoothGatt == null) {
            Logger.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        Logger.d(TAG, "Enable notifications-> " + characteristic.getUuid());

        //enable characteristic notifications
        bluetoothGatt.setCharacteristicNotification(characteristic, true);

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
                .fromString(CLIENT_CHARACTERISTIC_CONFIG));
        descriptor.setValue(type);
        bluetoothGatt.writeDescriptor(descriptor);
    }

    protected abstract void checkCharacteristics();

    protected abstract void checkDescriptorStatus(BluetoothGattDescriptor descriptor, int status);


    /**
     * Write to the characteristic
     *
     * @param characteristic BluetoothGattCharacteristic to be written
     * @param msg            message to be set on given characteristic
     */
    protected void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] msg) {
        //check mBluetoothGatt is available
        if (bluetoothGatt == null || characteristic == null) {
            return;
        }

        characteristic.setValue(msg);
        boolean started = bluetoothGatt.writeCharacteristic(characteristic);

        Logger.v(TAG, "Write characteristic-> "
                + BitwiseOperations.bytesToHexadecimalString(msg) + " - " + started);
    }

    /**
     * Read characteristic
     *
     * @param characteristic BluetoothGattCharacteristic to be read
     */
    protected void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        //check mBluetoothGatt is available
        if (bluetoothGatt == null || characteristic == null) {
            return;
        }

        boolean started = bluetoothGatt.readCharacteristic(characteristic);

        Logger.v(TAG, "Read characteristic-> " + characteristic.getUuid() + " - " + started);
    }


    /**
     * Calls the finish method and sends a callback error if the exception on the finish
     * method is thrown.
     */
    protected void finishWithWarning() {
        try {
            finish();

        } catch (Exception e) {
            Logger.e(TAG, "Failed finish", e);
        }
    }
}
