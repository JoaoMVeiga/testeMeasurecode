package com.pluxbiosignals.beurer.api.interfaces.devices;

public interface IGlucometer {

    void getAllRecords();

    void getFirstRecord();

    void getLastRecord();

    void getRecordsLessThenOrEqualTo(int highLimit);

    void getRecordsGreaterThenOrEqualTo(int lowLimit);

    void getStoredRecordsFromRange(int lowLimit, int highLimit);

    void getNumberOfStoredRecords();

    void abort();

    //TODO add delete methods!!
}
