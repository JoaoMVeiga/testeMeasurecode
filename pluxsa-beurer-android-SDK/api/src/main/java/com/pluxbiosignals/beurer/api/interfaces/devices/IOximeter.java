package com.pluxbiosignals.beurer.api.interfaces.devices;

public interface IOximeter {

    void getVersion();

    void setTimeSynchronization();

    void getDeviceTime();

    void getDataStorageInformation();

    void getMeasurementData(boolean deleteAfter);

}
