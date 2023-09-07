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

import com.pluxbiosignals.beurer.api.enums.glucometer.GlucLocation;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucMeasureFlag;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucType;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucUnit;
import com.pluxbiosignals.beurer.api.utils.ConversionUtils;
import com.pluxbiosignals.beurer.api.utils.Logger;

import java.util.Calendar;
import java.util.Objects;

public class GlucMeasurement implements Parcelable {

    private final GlucUnit unit;
    private final int seqNumber;
    private final long timestamp;
    private final Float glucoseConcentration;
    private final GlucType sampleType;
    private final GlucLocation sampleLocation;
    private final GlucSensorStatus status;

    public GlucMeasurement(GlucUnit unit, int seqNumber, long timestamp, Float glucoseConcentration,
                           GlucType sampleType, GlucLocation sampleLocation, GlucSensorStatus status) {
        this.unit = unit;
        this.seqNumber = seqNumber;
        this.timestamp = timestamp;
        this.glucoseConcentration = glucoseConcentration;
        this.sampleType = sampleType;
        this.sampleLocation = sampleLocation;
        this.status = status;
    }


    protected GlucMeasurement(Parcel in) {
        unit = GlucUnit.valueOf(in.readString());
        seqNumber = in.readInt();
        timestamp = in.readLong();
        if (in.readByte() == 0) {
            glucoseConcentration = null;
        } else {
            glucoseConcentration = in.readFloat();
        }
        sampleType = GlucType.valueOf(in.readString());
        sampleLocation = GlucLocation.valueOf(in.readString());
        status = in.readParcelable(GlucSensorStatus.class.getClassLoader());
    }

    public static final Creator<GlucMeasurement> CREATOR = new Creator<GlucMeasurement>() {
        @Override
        public GlucMeasurement createFromParcel(Parcel in) {
            return new GlucMeasurement(in);
        }

        @Override
        public GlucMeasurement[] newArray(int size) {
            return new GlucMeasurement[size];
        }
    };

    public static GlucMeasurement parseReply(byte[] receivedData) throws Exception {
        // Flag processing
        byte flag = receivedData[0];
        boolean hasTimeOffset = (GlucMeasureFlag.TIME_OFFSET.getValue() & flag) != 0;
        boolean hasTypeAndLocation = (GlucMeasureFlag.GLUCOSE_CONCENTRATION.getValue() & flag) != 0;
        final GlucUnit unit = GlucUnit.getUnit(GlucMeasureFlag.UNIT.getValue() & flag);
        boolean hasSensorStatusAnnunciation = (GlucMeasureFlag.SENSOR_STATUS_ANNUNCIATION.getValue() & flag) != 0;
        boolean contextInfoFollows = (GlucMeasureFlag.CONTEXT_INFORMATION_FOLLOW.getValue() & flag) != 0;

        // Sequence Number
        short nSeq = (short) (((receivedData[2] & 0xFF) << 8) | (receivedData[1] & 0xFF));

        // Base Time
        short year = (short) (((receivedData[4] & 0xFF) << 8) | (receivedData[3] & 0xFF));
        byte month = receivedData[5];
        byte day = receivedData[6];
        byte hour = receivedData[7];
        byte minute = receivedData[8];
        byte second = receivedData[9];

        // Calculate the timestamp using Calendar or other suitable method
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        long timestamp = calendar.getTimeInMillis() / 1000 * 1000;

        int offset = 10;

        // Time Offset
        short timeOffset = 0;
        if (hasTimeOffset) {
            timeOffset = (short) (((receivedData[offset + 1] & 0xFF) << 8) | (receivedData[offset] & 0xFF));
            offset += 2;
        }

        // Glucose Concentration
        float glucoseConcentration = 0.0f;
        GlucType type = GlucType.UNKNOWN;
        GlucLocation location = GlucLocation.SAMPLE_LOCATION_NOT_AVAILABLE;
        if (hasTypeAndLocation) {
            short glucoseConcentrationValue = (short) (((receivedData[offset + 1] & 0xFF) << 8) | (receivedData[offset] & 0xFF));
            glucoseConcentration = ConversionUtils.sFloat(glucoseConcentrationValue);

            byte sampleType = (byte) ((receivedData[offset + 2] & 0xF0) >> 4);
            type = GlucType.getType(sampleType);
            byte sampleLocation = (byte) (receivedData[offset + 2] & 0x0F);
            location = GlucLocation.getLocation(sampleLocation);

            offset += 3;
        }

        // Sensor Status Annunciation
        GlucSensorStatus status = null;
        if (hasSensorStatusAnnunciation) {
            short sensorAnnunciation = (short) (((receivedData[offset] & 0xFF) << 8)
                    | (receivedData[offset + 1] & 0xFF));
            status = GlucSensorStatus.parseReply(sensorAnnunciation);
        }

        if (contextInfoFollows) {
            Logger.d("TAG", "Has context info"); //TODO!!
        }

        return new GlucMeasurement(unit, nSeq, timestamp, glucoseConcentration, type, location, status);
    }

    @Override
    public String toString() {
        return "GlucMeasurement{" +
                "unit=" + unit +
                ", seqNumber=" + seqNumber +
                ", timestamp=" + timestamp +
                ", glucoseConcentration=" + glucoseConcentration +
                ", sampleType=" + sampleType +
                ", sampleLocation=" + sampleLocation +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlucMeasurement that = (GlucMeasurement) o;
        return seqNumber == that.seqNumber && timestamp == that.timestamp && unit == that.unit
                && Objects.equals(glucoseConcentration, that.glucoseConcentration)
                && sampleType == that.sampleType && sampleLocation == that.sampleLocation
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit, seqNumber, timestamp, glucoseConcentration, sampleType,
                sampleLocation, status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(unit.name());
        parcel.writeInt(seqNumber);
        parcel.writeLong(timestamp);
        if (glucoseConcentration == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(glucoseConcentration);
        }
        parcel.writeString(sampleType.name());
        parcel.writeString(sampleLocation.name());
        parcel.writeParcelable(status, i);
    }
}
