package com.pluxbiosignals.beurer.api.interfaces.devices;

import com.pluxbiosignals.beurer.api.objects.ScaleUser;

public interface IWeightScale {

    void setCurrentTime();

    void listAllUsers();

    void takeMeasurement();

    void createUser(ScaleUser user, int code);

    void consentProcedure(int index, int code);

    void deleteUserData();
}
