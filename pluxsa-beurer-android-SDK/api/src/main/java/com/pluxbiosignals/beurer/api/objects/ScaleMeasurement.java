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

import com.pluxbiosignals.beurer.api.enums.scale.BodyCompositionFlag;
import com.pluxbiosignals.beurer.api.enums.scale.ScaleUnit;
import com.pluxbiosignals.beurer.api.enums.scale.WeightMeasurementFlag;

import java.util.Calendar;
import java.util.Objects;

public class ScaleMeasurement implements Parcelable {

    private final ScaleUnit unit;
    private final float weight;
    private final Long timestamp;
    private final Integer userId;
    private final Float bmi;
    private final Float height;

    public ScaleMeasurement(ScaleUnit unit, float weight, Long timestamp, Integer userId, Float bmi,
                            Float height) {
        this.unit = unit;
        this.weight = weight;
        this.timestamp = timestamp;
        this.userId = userId;
        this.bmi = bmi;
        this.height = height;
    }

    protected ScaleMeasurement(Parcel in) {
        unit = ScaleUnit.valueOf(in.readString());
        weight = in.readFloat();
        if (in.readByte() == 0) {
            timestamp = null;
        } else {
            timestamp = in.readLong();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        if (in.readByte() == 0) {
            bmi = null;
        } else {
            bmi = in.readFloat();
        }
        if (in.readByte() == 0) {
            height = null;
        } else {
            height = in.readFloat();
        }
    }

    public static final Creator<ScaleMeasurement> CREATOR = new Creator<ScaleMeasurement>() {
        @Override
        public ScaleMeasurement createFromParcel(Parcel in) {
            return new ScaleMeasurement(in);
        }

        @Override
        public ScaleMeasurement[] newArray(int size) {
            return new ScaleMeasurement[size];
        }
    };

    public static ScaleMeasurement parseReply(byte[] data) {
        // Flag processing
        byte flag = data[0];
        final ScaleUnit unit = ScaleUnit.getUnit(BodyCompositionFlag.UNIT.getValue() & flag);
        boolean hasTimestamp = (WeightMeasurementFlag.TIMESTAMP.getValue() & flag) != 0;
        boolean hasUserId = (WeightMeasurementFlag.UNIT.getValue() & flag) != 0;
        boolean hasBmiHeight = (WeightMeasurementFlag.BMI_HEIGHT.getValue() & flag) != 0;

        // Weight
        short weightReceived = (short) (((data[2] & 0xFF) << 8) | (data[1] & 0xFF));
        float weight = unit.equals(ScaleUnit.SI) ? (float) weightReceived / 1000 * 5 :
                (float) weightReceived / 100;

        // Timestamp
        Long timestamp = null;
        if (hasTimestamp) {
            short year = (short) (((data[4] & 0xFF) << 8) | (data[3] & 0xFF));
            byte month = (byte) (data[5] & 0xFF);
            byte day = (byte) (data[6] & 0xFF);
            byte hour = (byte) (data[7] & 0xFF);
            byte minute = (byte) (data[8] & 0xFF);
            byte second = (byte) (data[9] & 0xFF);

            final Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day, hour, minute, second);
            timestamp = calendar.getTimeInMillis() / 1000 * 1000;
        }

        // User ID
        Integer userId = null;
        if (hasUserId) {
            userId = data[10] & 0xFF;
        }

        // BMI & Height
        Float height = null;
        Float bmi = null;
        if (hasBmiHeight) {
            short BMI = (short) (((data[12] & 0xFF) << 8) | (data[11] & 0xFF));
            bmi = (float) BMI / 10;

            short heightReceived = (short) (((data[14] & 0xFF) << 8) | (data[13] & 0xFF));
            height = unit.equals(ScaleUnit.SI) ? (float) heightReceived / 1000
                    : (float) heightReceived / 10;
        }

        return new ScaleMeasurement(unit, weight, timestamp, userId, bmi, height);
    }

    public ScaleUnit getUnit() {
        return unit;
    }

    public float getWeight() {
        return weight;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Integer getUserId() {
        return userId;
    }

    public Float getBmi() {
        return bmi;
    }

    public Float getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "ScaleMeasurement{" +
                "unit=" + unit +
                ", weight=" + weight +
                ", timestamp=" + timestamp +
                ", userId=" + userId +
                ", bmi=" + bmi +
                ", height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScaleMeasurement that = (ScaleMeasurement) o;
        return Float.compare(that.weight, weight) == 0 && unit == that.unit
                && Objects.equals(timestamp, that.timestamp)
                && Objects.equals(userId, that.userId)
                && Objects.equals(bmi, that.bmi)
                && Objects.equals(height, that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit, weight, timestamp, userId, bmi, height);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(unit.name());
        parcel.writeFloat(weight);
        if (timestamp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(timestamp);
        }
        if (userId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(userId);
        }
        if (bmi == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(bmi);
        }
        if (height == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(height);
        }
    }
}
