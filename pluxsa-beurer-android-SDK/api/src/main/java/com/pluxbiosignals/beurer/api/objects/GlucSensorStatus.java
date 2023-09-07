/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.pluxbiosignals.beurer.api.enums.glucometer.SensorStatusFlags;

import java.util.Objects;

public class GlucSensorStatus implements Parcelable {

    private final boolean lowBattery;
    private final boolean malfunction;
    private final boolean sampleSizeInsufficient;
    private final boolean stripInsertionError;
    private final boolean stripTypeIncorrect;
    private final boolean resultUpperBound;
    private final boolean resultLowerBound;
    private final boolean temperatureTooHigh;
    private final boolean temperatureTooLow;
    private final boolean stripPulledTooSoon;
    private final boolean generalFault;
    private final boolean timeFault;

    GlucSensorStatus(boolean lowBattery, boolean malfunction, boolean sampleSizeInsufficient,
                     boolean stripInsertionError, boolean stripTypeIncorrect, boolean resultUpperBound,
                     boolean resultLowerBound, boolean temperatureTooHigh, boolean temperatureTooLow,
                     boolean stripPulledTooSoon, boolean generalFault, boolean timeFault) {
        this.lowBattery = lowBattery;
        this.malfunction = malfunction;
        this.sampleSizeInsufficient = sampleSizeInsufficient;
        this.stripInsertionError = stripInsertionError;
        this.stripTypeIncorrect = stripTypeIncorrect;
        this.resultUpperBound = resultUpperBound;
        this.resultLowerBound = resultLowerBound;
        this.temperatureTooHigh = temperatureTooHigh;
        this.temperatureTooLow = temperatureTooLow;
        this.stripPulledTooSoon = stripPulledTooSoon;
        this.generalFault = generalFault;
        this.timeFault = timeFault;
    }


    protected GlucSensorStatus(Parcel in) {
        lowBattery = in.readByte() != 0;
        malfunction = in.readByte() != 0;
        sampleSizeInsufficient = in.readByte() != 0;
        stripInsertionError = in.readByte() != 0;
        stripTypeIncorrect = in.readByte() != 0;
        resultUpperBound = in.readByte() != 0;
        resultLowerBound = in.readByte() != 0;
        temperatureTooHigh = in.readByte() != 0;
        temperatureTooLow = in.readByte() != 0;
        stripPulledTooSoon = in.readByte() != 0;
        generalFault = in.readByte() != 0;
        timeFault = in.readByte() != 0;
    }

    public static final Creator<GlucSensorStatus> CREATOR = new Creator<GlucSensorStatus>() {
        @Override
        public GlucSensorStatus createFromParcel(Parcel in) {
            return new GlucSensorStatus(in);
        }

        @Override
        public GlucSensorStatus[] newArray(int size) {
            return new GlucSensorStatus[size];
        }
    };

    public static GlucSensorStatus parseReply(short sensorAnnunciation) {
        boolean lowBattery = (SensorStatusFlags.LOW_BATTERY.getValue() & sensorAnnunciation) != 0;
        boolean malfunction = (SensorStatusFlags.SENSOR_MALFUNCTION.getValue() & sensorAnnunciation) != 0;
        boolean sampleSizeInsufficient = (SensorStatusFlags.SAMPLE_SIZE_INSUFFICIENT.getValue() & sensorAnnunciation) != 0;
        boolean stripInsertionError = (SensorStatusFlags.STRIP_INSERTION_ERROR.getValue() & sensorAnnunciation) != 0;
        boolean stripTypeIncorrect = (SensorStatusFlags.STRIP_TYPE_INCORRECT.getValue() & sensorAnnunciation) != 0;
        boolean resultUpperBound = (SensorStatusFlags.RESULT_UPPER_BOUND.getValue() & sensorAnnunciation) != 0;
        boolean resultLowerBound = (SensorStatusFlags.RESULT_LOWER_BOUND.getValue() & sensorAnnunciation) != 0;
        boolean temperatureTooHigh = (SensorStatusFlags.SENSOR_TEMPERATURE_TOO_HIGH.getValue() & sensorAnnunciation) != 0;
        boolean temperatureTooLow = (SensorStatusFlags.SENSOR_TEMPERATURE_TOO_LOW.getValue() & sensorAnnunciation) != 0;
        boolean stripPulledTooSoon = (SensorStatusFlags.STRIP_PULLED_TOO_SOON.getValue() & sensorAnnunciation) != 0;
        boolean generalFault = (SensorStatusFlags.GENERAL_FAULT.getValue() & sensorAnnunciation) != 0;
        boolean timeFault = (SensorStatusFlags.TIME_FAULT.getValue() & sensorAnnunciation) != 0;

        return new GlucSensorStatus(lowBattery, malfunction, sampleSizeInsufficient,
                stripInsertionError, stripInsertionError, resultUpperBound, resultLowerBound,
                temperatureTooHigh, temperatureTooLow, stripPulledTooSoon, generalFault, timeFault);
    }

    public boolean isLowBattery() {
        return lowBattery;
    }

    public boolean isMalfunction() {
        return malfunction;
    }

    public boolean isSampleSizeInsufficient() {
        return sampleSizeInsufficient;
    }

    public boolean isStripInsertionError() {
        return stripInsertionError;
    }

    public boolean isStripTypeIncorrect() {
        return stripTypeIncorrect;
    }

    public boolean isResultUpperBound() {
        return resultUpperBound;
    }

    public boolean isResultLowerBound() {
        return resultLowerBound;
    }

    public boolean isTemperatureTooHigh() {
        return temperatureTooHigh;
    }

    public boolean isTemperatureTooLow() {
        return temperatureTooLow;
    }

    public boolean isStripPulledTooSoon() {
        return stripPulledTooSoon;
    }

    public boolean isGeneralFault() {
        return generalFault;
    }

    public boolean isTimeFault() {
        return timeFault;
    }

    @Override
    public String toString() {
        return "GlucSensorStatus{" +
                "lowBattery=" + lowBattery +
                ", malfunction=" + malfunction +
                ", sampleSizeInsufficient=" + sampleSizeInsufficient +
                ", stripInsertionError=" + stripInsertionError +
                ", stripTypeIncorrect=" + stripTypeIncorrect +
                ", resultUpperBound=" + resultUpperBound +
                ", resultLowerBound=" + resultLowerBound +
                ", temperatureTooHigh=" + temperatureTooHigh +
                ", temperatureTooLow=" + temperatureTooLow +
                ", stripPulledTooSoon=" + stripPulledTooSoon +
                ", generalFault=" + generalFault +
                ", timeFault=" + timeFault +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlucSensorStatus that = (GlucSensorStatus) o;
        return lowBattery == that.lowBattery && malfunction == that.malfunction
                && sampleSizeInsufficient == that.sampleSizeInsufficient
                && stripInsertionError == that.stripInsertionError
                && stripTypeIncorrect == that.stripTypeIncorrect
                && resultUpperBound == that.resultUpperBound
                && resultLowerBound == that.resultLowerBound
                && temperatureTooHigh == that.temperatureTooHigh
                && temperatureTooLow == that.temperatureTooLow
                && stripPulledTooSoon == that.stripPulledTooSoon
                && generalFault == that.generalFault && timeFault == that.timeFault;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowBattery, malfunction, sampleSizeInsufficient, stripInsertionError,
                stripTypeIncorrect, resultUpperBound, resultLowerBound, temperatureTooHigh,
                temperatureTooLow, stripPulledTooSoon, generalFault, timeFault);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeByte((byte) (lowBattery ? 1 : 0));
        parcel.writeByte((byte) (malfunction ? 1 : 0));
        parcel.writeByte((byte) (sampleSizeInsufficient ? 1 : 0));
        parcel.writeByte((byte) (stripInsertionError ? 1 : 0));
        parcel.writeByte((byte) (stripTypeIncorrect ? 1 : 0));
        parcel.writeByte((byte) (resultUpperBound ? 1 : 0));
        parcel.writeByte((byte) (resultLowerBound ? 1 : 0));
        parcel.writeByte((byte) (temperatureTooHigh ? 1 : 0));
        parcel.writeByte((byte) (temperatureTooLow ? 1 : 0));
        parcel.writeByte((byte) (stripPulledTooSoon ? 1 : 0));
        parcel.writeByte((byte) (generalFault ? 1 : 0));
        parcel.writeByte((byte) (timeFault ? 1 : 0));
    }
}
