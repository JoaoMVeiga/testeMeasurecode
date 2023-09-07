# Beurer Android BLE - Application Programming Interface #

This library offers a collection of classes that simplify communication with different Beurer BLE devices.

## Introduction

The Beurer Android BLE API provides a convenient way to communicate with Beurer health monitoring devices over Bluetooth Low Energy (BLE).

#### Current Version settings

* Version Name: 1.0.0
* Version Code: 1

[![min sdk](https://img.shields.io/badge/min%20sdk-28-orange?logo=android)](https://android-arsenal.com/api?level=28#l28)  [![target sdk](https://img.shields.io/badge/target%20sdk-33-green?logo=android)](https://android-arsenal.com/api?level=33#l33)

## Key Features

- **Easy Communication**: Simplified methods for effortless communication.
- **Bluetooth Scanning**: Seamless device discovery.
- **Callback Mechanisms**: Callback mechanisms for data reception.
- **Diverse Device Coverage**: Compatible with various Beurer devices.

    + Glucometer GL50
    + Oximeter PO60
    + Thermometer OT30
    + Blood Pressure Monitor BM57
    + Weight Scale BF600

## Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- Android device with Bluetooth Low Energy (BLE) support

### Installation

To use this library in your Android project, follow these steps:

1. Clone or download this repository.
2. In Android Studio, go to `File > New > Import Module`.
3. Select the downloaded library directory and click `Finish`.
4. In your app's `build.gradle` file, add the library module as a dependency:

```gradle
dependencies {
    // ... other dependencies
    implementation project(':api')
}
```

## Usage

### Device Scan
Utilize the `DeviceScan` class to initiate device scanning. Configure the reception of scanned devices by employing `setCallback(IDeviceFound iDeviceFound)`. To manage the scan workflow, use the `startScan` and `stopScan` methods.

When a device is detected, it's delivered through the `onDeviceFound(BluetoothDevice bluetoothDevice, Devices device)` callback.

### Device Communication
To access the communication class for interacting with individual devices, employ the `getCommunication(Devices deviceType, Context context)` method from the `CommunicationFactory`.

Next, initialize the communication class using `init(IDeviceCallback deviceCallback)` to receive data through the appropriate callback based on the device type:

| Communication Class | Corresponding Callback Interface | Description                                              |
|:--------------------|:---------------------------------|:---------------------------------------------------------|
| BeurerGlucometer    | `IGlucCallback`                  | Callbacks for receiving glucometer device data.          |
| BeurerOximeter      | `IOximeterCallback`              | Callbacks for receiving oximeter device data.            |
| BeurerThermometer   | `IGenericCallback`               | Generic callbacks for receiving thermometer device data. |
| BeurerBloodPressure | `IGenericCallback`               | Generic callbacks for receiving blood pressure data.     |
| BeurerWeightScale   | `IScaleCallback`                 | Callbacks for receiving weight scale device data.        |

Manage communication state by using `connect(String address)` and `disconnect()` methods. When you are done with the communication make sure to call `finish()`.

**Note**: When using the Beurer devices, it's important to follow specific procedures to ensure successful communication and data retrieval. Below are instructions for interacting with the different supported Beurer devices.

#### 1. Beurer Glucometer (GL 50)

To communicate with a Beurer Glucometer GL 50 device you need an instance of the `BeurerGlucometer` class.
The measured values can be transferred after a measurement and in the memory recall mode.

| Method                                                 | Description                                                                    |
|:-------------------------------------------------------|:-------------------------------------------------------------------------------|
| `getAllRecords()`                                      | Requests all measurements. Returns measurements and context via callback.      |
| `getFirstRecord()`                                     | Requests the oldest measurement. Returns measurement and context via callback. |
| `getRecordsLessThanOrEqualTo(int highLimit)`           | Requests records below or equal to `highLimit`. Returns data via callback.     |
| `getRecordsGreaterThanOrEqualTo(int lowLimit)`         | Requests records greater or equal to `lowLimit`. Returns data via callback.    |
| `getStoredRecordsInRange(int lowLimit, int highLimit)` | Requests records within range. Returns data via callback.                      |
| `getNumberOfStoredRecords()`                           | Requests total number of stored records. Receives count via callback.          |

State updates and answers to above methods are received via callbacks.

| Callback                                                                              | Description                                           |
|:--------------------------------------------------------------------------------------|:------------------------------------------------------|
| `onDeviceStateChanged(States state)`                                                  | Called when the device's state changes.               |
| `onResults(ArrayList<GlucMeasurement> measurements, ArrayList<GlucContext> contexts)` | Called when results are requested.                    |
| `onTotalRecords(int total)`                                                           | Called when the total number of records is requested. |

#### 2. Beurer Oximeter (PO 60)

There are two different ways to enable device advertising.

1. Long press the function key while the pulse oximeter is turned off for 5 seconds
2. Acquire data and remove the finger

'SYNC' will appear on the oximeter screen when the device is ready for data transfer. At this point, the device will appear during the Bluetooth scan.

The first time the device is configured, a 6-digit pairing code appears on the oximeter. This code needs to be entered in the app when requested.

To communicate with a Beurer Oximeter PO 60 device you need an instance of the `BeurerOximeter` class.


| Method                                    | Description                                                                                                                |
|:------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------|
| `getVersion()`                            | Requests HW & FW versions and returns it via callback.                                                                     |
| `setTimeSynchronization()`                | Requests time synchronization.                                                                                             |
| `getDeviceTime()`                         | Requests device time and returns it via callback.                                                                          |
| `getDataStorageInformation()`             | Retrieves storage info and returns it via callback.                                                                        |
| `getMeasurementData(boolean deleteAfter)` | Requests measurement data and returns it via callback. If `delete` is set to true, stored data will be deleted afterwards. |

State updates and answers to above methods are received via callbacks.

| Callback                                      | Description                                       |
|:----------------------------------------------|:--------------------------------------------------|
| `onDeviceStateChanged(States state)`          | Called when the device's state changes.           |
| `onVersion(OximeterVersion version)`          | Called when device version is requested.          |
| `onTime(OximeterTime time)`                   | Called when device time is requested.             |
| `onStorageInfo(OximeterStorage storage)`      | Called when device storage info is requested.     |
| `onData(ArrayList<OximeterMeasure> measures)` | Called when device measurement data is requested. |
| `onSetTimeResult(boolean success)`            | Indicates the result of the time synchronization. |

#### 3. Beurer Thermometer (OT 30)

The first time the device is used, the pairing request must be accepted. Follow these steps:

1. Turn on the device by clicking the button.
2. When the device is ready (shows 'Lo' on the display), click the button again.
3. The device will appear during the scan.
4. Connect to the device.
5. Accept the pairing request using the 6-digit pairing code displayed on screen.

After pairing, the device will be ready to use. The data stored on the device can now be received.

To communicate with a Beurer Thermometer OT 30 device you need an instance of the `BeurerThermometer` class.

State updates and stored data are received via callbacks.

| Callback                             | Description                                                                             |
|:-------------------------------------|:----------------------------------------------------------------------------------------|
| `onDeviceStateChanged(States state)` | Called when the device's state changes.                                                 |
| `onData(Parcelable data)`            | Called when temperature measurement  is received. Here `data` is of type `Temperature`. |

#### 4. Beurer Blood Pressure Monitor (BM 57)

Data is received when memory is accessed (M1 or M2) and when a new measure is taken.

To communicate with a Beurer Blood Pressure monitor BM 57 device you need an instance of the `BeurerBloodPressure` class.

State updates and stored data are received via callbacks.

| Callback                             | Description                                                                                  |
|:-------------------------------------|:---------------------------------------------------------------------------------------------|
| `onDeviceStateChanged(States state)` | Called when the device's state changes.                                                      |
| `onData(Parcelable data)`            | Called when blood pressure measurement  is received. Here `data` is of type `BPMeasurement`. |

#### 5. Beurer Weight Scale (BF 600)

The first time the device is used, accept the pairing request. If the device has never been configured it is necessary to set its time before any use.

To communicate with a Beurer Weight Scale BF 600 device you need an instance of the `BeurerWeightScale` class.

Here's how to use the Beurer Weight Scale:
1. Create a new user by providing details and the consent code.
2. The new user index is returned via callback.
3. Use the received user index and consent code to select the user.
4. Initiate a measurement for the selected user.

| Method                                             | Description                                                                                                                                                    |
|:---------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `setCurrentTime()`                                 | Requests time synchronization.                                                                                                                                 |
| `listAllUsers()`                                   | Requests user list. List is returned via callback.                                                                                                             |
| `takeMeasurement()`                                | Requests a measurement to be taken. Should only be used after user consent is given.                                                                           |
| `createUser(ScaleUser newUser, int consentCode)`   | Request user creation. Provided `consentCode` will need to be provided before requesting a measurement for this user. New user index is received via callback. |
| `consentProcedure(int userIndex, int consentCode)` | Gives consent for the user with the provided `userIndex`. The `consentCode` should be the one provided when the user was created.                              |
| `deleteUserData( )`                                | Requests delete of the user whose consent is given at the time of the call.                                                                                    |

State updates and answers to above methods are received via callbacks.

| Callback                                                                                                  | Description                                     |
|:----------------------------------------------------------------------------------------------------------|:------------------------------------------------|
| `onDeviceStateChanged(States state)`                                                                      | Called when the device's state changes.         |
| `onResults(MeasurementStatus status, ScaleMeasurement measurement, ScaleBodyComposition bodyComposition)` | Called when a measurement is received.          |
| `onUserList(ArrayList<ScaleUser> users)`                                                                  | Called when user list is requested.             |
| `onUserCreated(Integer index)`                                                                            | Called after a new user is created.             |
| `onUserDataDeleted(boolean success)`                                                                      | Indicates the result of user delete.            |
| `onConsent(boolean success)`                                                                              | Indicates the result of user consent procedure. |

### Example

```java
// Scan for devices
DeviceScan deviceScanner = new DeviceScan();
deviceScanner.setCallback(this);
deviceScanner.startScan();

        (...)

// Create communication instance 
BeurerLECommunication communication = CommunicationFactory.getCommunication(deviceType, context);

// Set up a callback to receive data
communication.init(this);

// Connect to the device
communication.connect(macAddress)

        (...)

// Finish communication
communication.finish()

```