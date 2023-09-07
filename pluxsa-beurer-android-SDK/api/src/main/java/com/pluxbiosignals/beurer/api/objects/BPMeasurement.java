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

import static com.pluxbiosignals.beurer.api.utils.ConversionUtils.sFloat;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.pluxbiosignals.beurer.api.enums.bpm.BpmFlag;
import com.pluxbiosignals.beurer.api.enums.bpm.BpmUnit;

import java.util.Calendar;
import java.util.Objects;

public class BPMeasurement implements Parcelable {

    private final BpmUnit unit;
    private final float systolic;
    private final float diastolic;
    private final float meanArterialPressure;
    private final long timestamp;
    private final float pulseRate;
    private final Integer userId;

    private final BpmMeasureStatus status;


    public BPMeasurement(BpmUnit unit, float systolic, float diastolic, float meanArterialPressure,
                         Long timestamp, Float pulseRate, Integer userId, BpmMeasureStatus status) {
        this.unit = unit;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.meanArterialPressure = meanArterialPressure;
        this.timestamp = timestamp;
        this.pulseRate = pulseRate;
        this.userId = userId;
        this.status = status;
    }

    protected BPMeasurement(Parcel in) {
        unit = BpmUnit.valueOf(in.readString());
        systolic = in.readFloat();
        diastolic = in.readFloat();
        meanArterialPressure = in.readFloat();
        timestamp = in.readLong();
        pulseRate = in.readFloat();
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        status = in.readParcelable(BpmMeasureStatus.class.getClassLoader());
    }

    public static final Creator<BPMeasurement> CREATOR = new Creator<BPMeasurement>() {
        @Override
        public BPMeasurement createFromParcel(Parcel in) {
            return new BPMeasurement(in);
        }

        @Override
        public BPMeasurement[] newArray(int size) {
            return new BPMeasurement[size];
        }
    };

    public static BPMeasurement parseReply(byte[] receivedData) throws Exception {
        // Flag processing
        byte flag = receivedData[0];
        final BpmUnit unit = BpmUnit.getUnit((BpmFlag.UNITS.getValue() & flag));
        boolean hasTimestamp = (BpmFlag.TIMESTAMP.getValue() & flag) != 0;
        boolean hasPulseRate = (BpmFlag.PULSE_RATE.getValue() & flag) != 0;
        boolean hasUserId = (BpmFlag.USER_ID.getValue() & flag) != 0;
        boolean hasMeasurementStatus = (BpmFlag.MEASUREMENT_STATUS.getValue() & flag) != 0;
        boolean hasHsd = (BpmFlag.HSD.getValue() & flag) != 0;

        // Pressures
        short systolicValue = (short) (((receivedData[2] & 0xFF) << 8) | (receivedData[1] & 0xFF));
        float systolicPressure = sFloat(systolicValue);

        short diastolicValue = (short) (((receivedData[4] & 0xFF) << 8) | (receivedData[3] & 0xFF));
        float diastolicPressure = sFloat(diastolicValue);

        short mapValue = (short) (((receivedData[6] & 0xFF) << 8) | (receivedData[5] & 0xFF));
        float map = sFloat(mapValue);

        // Timestamp
        Long timestamp = null;
        if (hasTimestamp) {
            short year = (short) (((receivedData[8] & 0xFF) << 8) | (receivedData[7] & 0xFF));
            byte month = receivedData[9];
            byte day = receivedData[10];
            byte hour = receivedData[11];
            byte minute = receivedData[12];
            byte second = receivedData[13];

            // Calculate the timestamp using Calendar or other suitable method
            final Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day, hour, minute, second);
            timestamp = calendar.getTimeInMillis() / 1000 * 1000;
        }

        Float pr = null;//TODO handle different models
        if (hasPulseRate) {
            short prValue = (short) (((receivedData[15] & 0xFF) << 8) | (receivedData[14] & 0xFF));
            pr = sFloat(prValue);
        }

        Integer userId = null;
        if (hasUserId) {
            userId = (int) receivedData[16];
        }

        BpmMeasureStatus measureStatus = null;
        if (hasMeasurementStatus) {
            short measurementStatus = (short) (((receivedData[17] & 0xFF) << 8) | (receivedData[18] & 0xFF));
            measureStatus = BpmMeasureStatus.parseReply(measurementStatus, hasHsd);
        }

        return new BPMeasurement(unit, systolicPressure, diastolicPressure, map, timestamp, pr, userId,
                measureStatus);
    }

    public BpmUnit getUnit() {
        return unit;
    }

    public float getSystolic() {
        return systolic;
    }

    public float getDiastolic() {
        return diastolic;
    }

    public float getMeanArterialPressure() {
        return meanArterialPressure;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getPulseRate() {
        return pulseRate;
    }

    public int getUserId() {
        return userId;
    }

    public BpmMeasureStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "BPMeasurement{" +
                "unit=" + unit +
                ", systolic=" + systolic +
                ", diastolic=" + diastolic +
                ", meanArterialPressure=" + meanArterialPressure +
                ", timestamp=" + timestamp +
                ", pulseRate=" + pulseRate +
                ", userId=" + userId +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BPMeasurement that = (BPMeasurement) o;
        return Float.compare(that.systolic, systolic) == 0
                && Float.compare(that.diastolic, diastolic) == 0
                && Float.compare(that.meanArterialPressure, meanArterialPressure) == 0
                && timestamp == that.timestamp && Float.compare(that.pulseRate, pulseRate) == 0
                && unit == that.unit && Objects.equals(userId, that.userId)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit, systolic, diastolic, meanArterialPressure, timestamp, pulseRate,
                userId, status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(unit.name());
        parcel.writeFloat(systolic);
        parcel.writeFloat(diastolic);
        parcel.writeFloat(meanArterialPressure);
        parcel.writeLong(timestamp);
        parcel.writeFloat(pulseRate);
        if (userId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(userId);
        }
        parcel.writeParcelable(status, i);
    }
}
