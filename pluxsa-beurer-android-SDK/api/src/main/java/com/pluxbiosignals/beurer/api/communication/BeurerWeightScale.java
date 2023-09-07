/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.communication;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import com.pluxbiosignals.beurer.api.DeviceScan;
import com.pluxbiosignals.beurer.api.communication.base.BeurerLECommunication;
import com.pluxbiosignals.beurer.api.enums.scale.ControlPointReplyType;
import com.pluxbiosignals.beurer.api.enums.scale.ControlPointStatus;
import com.pluxbiosignals.beurer.api.enums.scale.Gender;
import com.pluxbiosignals.beurer.api.enums.scale.MeasurementStatus;
import com.pluxbiosignals.beurer.api.enums.scale.UserListReplyType;
import com.pluxbiosignals.beurer.api.enums.scale.WeightScaleCommands;
import com.pluxbiosignals.beurer.api.interfaces.IScaleCallback;
import com.pluxbiosignals.beurer.api.interfaces.devices.IWeightScale;
import com.pluxbiosignals.beurer.api.objects.ScaleBodyComposition;
import com.pluxbiosignals.beurer.api.objects.ScaleMeasurement;
import com.pluxbiosignals.beurer.api.objects.ScaleUser;
import com.pluxbiosignals.beurer.api.utils.BitwiseOperations;
import com.pluxbiosignals.beurer.api.utils.Logger;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Communication class for handling interactions with Beurer weight scale device.
 * <p>
 * Notes:
 * To make measurements, read or change the user data you
 * have to assign the user with the “consent procedure” after the user was created.
 */
public class BeurerWeightScale extends BeurerLECommunication implements IWeightScale {

    /* *
     * SERVICES
     *  */

    // GATT services
    private BluetoothGattService customService;
    private BluetoothGattService userDataService;
    private BluetoothGattService bodyCompositionService;
    private BluetoothGattService currentTimeService;

    /* *
     * SERVICES & CHARACTERISTICS
     *  */

    // weight scale service (main service)
    private static final UUID chWeightScaleMeasure = UUID.fromString("00002A9D-0000-1000-8000-00805f9b34fb");//indicate

    // body composition
    private static final UUID bodyCompositionServiceUUID = UUID.fromString("0000181B-0000-1000-8000-00805f9b34fb");
    private static final UUID chBodyCompositionMeasure = UUID.fromString("00002A9C-0000-1000-8000-00805f9b34fb");//indicate


    // user data service
    private static final UUID userDataServiceUUID = UUID.fromString("0000181C-0000-1000-8000-00805f9b34fb");
    private static final UUID chControlPoint = UUID.fromString("00002A9F-0000-1000-8000-00805f9b34fb");//write, indicate
    private static final UUID chDoB = UUID.fromString("00002A85-0000-1000-8000-00805f9b34fb");//read, write
    private static final UUID chGender = UUID.fromString("00002A8C-0000-1000-8000-00805f9b34fb");//read, write
    private static final UUID chHeight = UUID.fromString("00002A8E-0000-1000-8000-00805f9b34fb");//read, write
    private static final UUID chDBIncrement = UUID.fromString("00002A99-0000-1000-8000-00805f9b34fb");//read, write, notify

    // custom service
    private static final UUID customServiceUUID = UUID.fromString("0000FFF0-0000-1000-8000-00805f9b34fb");
    private static final UUID chUserList = UUID.fromString("0000FFF2-0000-1000-8000-00805f9b34fb");//write, notify
    private static final UUID chActivityLevel = UUID.fromString("0000FFF3-0000-1000-8000-00805f9b34fb");//read, write
    private static final UUID chTakeMeasurement = UUID.fromString("0000FFF4-0000-1000-8000-00805f9b34fb");//write, notify

    //current time
    private static final UUID currentTimeServiceUUID = UUID.fromString("00001805-0000-1000-8000-00805f9b34fb");
    private static final UUID chCurrentTime = UUID.fromString("00002A2B-0000-1000-8000-00805f9b34fb");//read, notify, write


    private boolean settingUserData = false;
    private ScaleUser currentUser;

    private final ArrayList<ScaleUser> users = new ArrayList<>();
    private ScaleMeasurement measurement;
    private ScaleBodyComposition bodyComposition;

    /**
     * Communication constructor, sets application context
     *
     * @param context communication context
     */
    public BeurerWeightScale(Context context) {
        super(context);

        mainServiceUUID = DeviceScan.ktScaleServiceUUID;//weight scale service
    }

    @Override
    protected void onCharacteristicWriteStatus(UUID uuid, int status) {
        if (!settingUserData) {
            return;
        }

        if (uuid.equals(chDoB)) {
            setUserGender(currentUser.getGender());

        } else if (uuid.equals(chGender)) {
            setUserHeight(currentUser.getHeight());

        } else if (uuid.equals(chHeight)) {
            setUserActivity(currentUser.getActivityLevel());

        } else if (uuid.equals(chActivityLevel)) {
            setDBIncrement();

            settingUserData = false;

            if (callback != null) {
                ((IScaleCallback) callback).onUserCreated(currentUser.getUserIndex());
            }
        }
    }

    @Override
    protected void dealWithReceivedData(UUID uuid, byte[] data) {
        Logger.d(TAG, "Received from " + uuid.toString() + " - "
                + BitwiseOperations.bytesToHexadecimalString(data));

        if (uuid.equals(chUserList)) {
            checkUserListReply(data);

        } else if (uuid.equals(chTakeMeasurement)) {
            final MeasurementStatus status = MeasurementStatus.getStatus(data[0]);

            if (callback != null) {
                ((IScaleCallback) callback).onResults(status, measurement, bodyComposition);
            }
            measurement = null;
            bodyComposition = null;

        } else if (uuid.equals(chWeightScaleMeasure)) {
            measurement = ScaleMeasurement.parseReply(data);

        } else if (uuid.equals(chBodyCompositionMeasure)) {
            bodyComposition = ScaleBodyComposition.parseReply(data);

        } else if (uuid.equals(chControlPoint)) {
            checkControlPointStatus(data);

        } else if (uuid.equals(chDBIncrement)) {
            Logger.d(TAG, "DB increment");

        }
    }

    private void checkControlPointStatus(byte[] data) {
        if (data[0] != 0x20) {
            return;
        }

        final ControlPointReplyType type = ControlPointReplyType.getReplyType(data[1]);
        final ControlPointStatus status = ControlPointStatus.getControlPointStatus(data[2]);

        boolean successful = status.equals(ControlPointStatus.SUCCESS);

        Logger.d(TAG, "Control point -> " + type + " - " + status);

        switch (type) {
            case NEW_USER_REPLY:
                Integer userIndex = null;
                if (successful) {
                    userIndex = data[3] & 0xFF;
                    Logger.d(TAG, "Created user with index " + userIndex);

                    consentProcedure(userIndex, 1234);

                    currentUser.setUserIndex(userIndex);
                }
                break;

            case CONSENT_REPLY:
                if (callback != null) {
                    ((IScaleCallback) callback).onConsent(successful);
                }

                long timestamp = currentUser.getDateOfBirth();
                setUserDoB(timestamp);
                break;

            case DELETE_USER_REPLY:
                if (callback != null) {
                    ((IScaleCallback) callback).onUserDataDeleted(successful);
                }
                break;

            default:
                Logger.d(TAG, "Unknown control point type");
                break;
        }
    }

    private void checkUserListReply(byte[] data) {
        final UserListReplyType type = UserListReplyType.getReplyType(data[0]);

        switch (type) {
            case USER:
                try {
                    ScaleUser user = ScaleUser.parseReply(data);
                    users.add(user);
                    Logger.d(TAG, "User " + user);

                } catch (Exception e) {
                    Logger.e(TAG, "Failed to parse user", e);
                }
                break;

            case SUCCESS:
            case NO_USERS:
                Logger.d(TAG, "User list ready");
                if (callback != null) {
                    ((IScaleCallback) callback).onUserList(users);
                }
                users.clear();
                break;

            default:
                Logger.d(TAG, "Unknown reply type");
                break;
        }
    }

    @Override
    protected void checkDiscoveredServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            finishWithWarning();
            return;
        }

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            Logger.d(TAG, "Gatt service: " + gattService.getUuid().toString());

            if (gattService.getUuid().equals(UUID.fromString(mainServiceUUID))) {
                deviceMainService = gattService;
                Logger.v(TAG, "Device Service Found");

            } else if (gattService.getUuid().equals(customServiceUUID)) {
                customService = gattService;
                Logger.v(TAG, "Custom Service Found");

            } else if (gattService.getUuid().equals(userDataServiceUUID)) {
                Logger.v(TAG, "User Data Service Found");
                userDataService = gattService;

            } else if (gattService.getUuid().equals(bodyCompositionServiceUUID)) {
                Logger.v(TAG, "Body Composition Service Found");
                bodyCompositionService = gattService;

            } else if (gattService.getUuid().equals(currentTimeServiceUUID)) {
                currentTimeService = gattService;
            }
        }

        checkCharacteristics();
    }

    @Override
    protected void checkCharacteristics() {
        final BluetoothGattCharacteristic chTime = currentTimeService.getCharacteristic(chCurrentTime);
        setCharacteristicConfigDescriptor(chTime, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
    }

    @Override
    protected void checkDescriptorStatus(BluetoothGattDescriptor descriptor, int status) {
        if (descriptor.getCharacteristic().getUuid().equals(chCurrentTime)) {
            final BluetoothGattCharacteristic weightMeasure = deviceMainService.getCharacteristic(chWeightScaleMeasure);
            setCharacteristicConfigDescriptor(weightMeasure, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);

        } else if (descriptor.getCharacteristic().getUuid().equals(chWeightScaleMeasure)) {
            final BluetoothGattCharacteristic chMeasure = bodyCompositionService.getCharacteristic(chBodyCompositionMeasure);
            setCharacteristicConfigDescriptor(chMeasure, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);

        } else if (descriptor.getCharacteristic().getUuid().equals(chBodyCompositionMeasure)) {
            final BluetoothGattCharacteristic chCPoint = userDataService.getCharacteristic(chControlPoint);
            setCharacteristicConfigDescriptor(chCPoint, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);

        } else if (descriptor.getCharacteristic().getUuid().equals(chControlPoint)) {
            final BluetoothGattCharacteristic chList = customService.getCharacteristic(chUserList);
            setCharacteristicConfigDescriptor(chList, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        } else if (descriptor.getCharacteristic().getUuid().equals(chUserList)) {
            final BluetoothGattCharacteristic chTakeMeasure = customService.getCharacteristic(chTakeMeasurement);
            setCharacteristicConfigDescriptor(chTakeMeasure, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        } else if (descriptor.getCharacteristic().getUuid().equals(chTakeMeasurement)) {
            final BluetoothGattCharacteristic chTime = userDataService.getCharacteristic(chDBIncrement);
            setCharacteristicConfigDescriptor(chTime, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        }
    }

    @Override
    public void setCurrentTime() {
        byte[] command = WeightScaleCommands.SET_TIME.getCommand(null);
        writeCharacteristic(currentTimeService.getCharacteristic(chCurrentTime), command);
    }

    @Override
    public void listAllUsers() {
        byte[] command = WeightScaleCommands.LIST_USERS.getCommand(null);
        writeCharacteristic(customService.getCharacteristic(chUserList), command);
    }

    @Override
    public void takeMeasurement() {
        byte[] command = WeightScaleCommands.TAKE_MEASURE.getCommand(null);
        writeCharacteristic(customService.getCharacteristic(chTakeMeasurement), command);
    }

    @Override
    public void createUser(ScaleUser user, int consentCode) {
        settingUserData = true;

        //The consent code is a number between 0-9999 (0x0000 to 0x270F) and is needed to login
        byte[] command = WeightScaleCommands.CREATE_USER.getCommand(new Integer[]{consentCode});
        writeCharacteristic(userDataService.getCharacteristic(chControlPoint), command);

        currentUser = user;
    }

    @Override
    public void consentProcedure(int userIndex, int consentCode) {
        byte[] command = WeightScaleCommands.CONSENT.getCommand(new Integer[]{userIndex, consentCode});
        writeCharacteristic(userDataService.getCharacteristic(chControlPoint), command);
    }

    @Override
    public void deleteUserData() {
        byte[] command = WeightScaleCommands.DELETE_USER_DATA.getCommand(null);
        writeCharacteristic(userDataService.getCharacteristic(chControlPoint), command);
    }

    private void setUserDoB(long timestamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        byte[] commandDoB = WeightScaleCommands.SET_USER_DOB.getCommand(new Integer[]{year, month, day});
        writeCharacteristic(userDataService.getCharacteristic(chDoB), commandDoB);
    }

    private void setUserHeight(int height) {
        byte[] commandHeight = WeightScaleCommands.SET_USER_HEIGHT
                .getCommand(new Integer[]{height});
        writeCharacteristic(userDataService.getCharacteristic(chHeight), commandHeight);
    }

    private void setUserGender(Gender gender) {
        byte[] commandGender = WeightScaleCommands.SET_USER_GENDER
                .getCommand(new Integer[]{gender.ordinal()});
        writeCharacteristic(userDataService.getCharacteristic(chGender), commandGender);
    }

    private void setUserActivity(int activity) {
        byte[] commandActivity = WeightScaleCommands.SET_USER_ACTIVITY
                .getCommand(new Integer[]{activity});
        writeCharacteristic(customService.getCharacteristic(chActivityLevel), commandActivity);
    }

    private void setDBIncrement() {
        byte[] increment = ByteBuffer.allocate(4).putInt(1).array();
        writeCharacteristic(userDataService.getCharacteristic(chDBIncrement), new byte[]{increment[3]});
    }
}
