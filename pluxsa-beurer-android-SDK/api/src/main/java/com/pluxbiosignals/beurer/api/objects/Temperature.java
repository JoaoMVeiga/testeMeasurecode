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

import static com.pluxbiosignals.beurer.api.utils.ConversionUtils.ieeeFloat;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.pluxbiosignals.beurer.api.enums.thermometer.TemperatureFlag;
import com.pluxbiosignals.beurer.api.enums.thermometer.TemperatureType;
import com.pluxbiosignals.beurer.api.enums.thermometer.TemperatureUnit;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class Temperature implements Parcelable {
    private final TemperatureUnit unit;
    private final Long timestamp;
    private final TemperatureType type;
    private final boolean fever;
    private final float temperature;

    Temperature(TemperatureUnit unit, Long timestamp, TemperatureType type, boolean fever,
                float temperature) {
        this.unit = unit;
        this.timestamp = timestamp;
        this.type = type;
        this.fever = fever;
        this.temperature = temperature;
    }


    protected Temperature(Parcel in) {
        byte tempByte = in.readByte();
        if (tempByte == 0) {
            timestamp = null;
        } else {
            timestamp = in.readLong();
        }
        fever = in.readByte() != 0;
        temperature = in.readFloat();
        unit = TemperatureUnit.valueOf(in.readString());
        type = TemperatureType.valueOf(in.readString());
    }

    public static final Creator<Temperature> CREATOR = new Creator<Temperature>() {
        @Override
        public Temperature createFromParcel(Parcel in) {
            return new Temperature(in);
        }

        @Override
        public Temperature[] newArray(int size) {
            return new Temperature[size];
        }
    };

    public static Temperature parseReply(byte[] receivedData) throws Exception {
        // Flag processing
        byte flag = receivedData[0];
        final TemperatureUnit unit = TemperatureUnit.getUnit((TemperatureFlag.UNITS.getFlag() & flag));
        boolean hasTimestamp = (TemperatureFlag.TIMESTAMP.getFlag() & flag) != 0;
        boolean hasType = (TemperatureFlag.TYPE.getFlag() & flag) != 0;
        boolean hasFever = (TemperatureFlag.FEVER.getFlag() & flag) != 0;

        // Temperature Value
        float temperature = ieeeFloat(Arrays.copyOfRange(receivedData, 1, 5));

        // Base Time
        int offset = 5;
        Long timestamp = null;
        if (hasTimestamp) {
            short year = (short) (((receivedData[6] & 0xFF) << 8) | (receivedData[5] & 0xFF));
            byte month = receivedData[7];
            byte day = receivedData[8];
            byte hour = receivedData[9];
            byte minute = receivedData[10];
            byte second = receivedData[11];
            offset += 7;


            final Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day, hour, minute, second);
            timestamp = calendar.getTimeInMillis() / 1000 * 1000;
        }

        TemperatureType temperatureType = TemperatureType.RESERVED;
        if (hasType) {
            byte type = receivedData[offset];
            temperatureType = TemperatureType.getReplyType(type);
        }

        return new Temperature(unit, timestamp, temperatureType, hasFever, temperature);
    }

    public TemperatureUnit getUnit() {
        return unit;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TemperatureType getType() {
        return type;
    }

    public boolean isFever() {
        return fever;
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "unit=" + unit +
                ", timestamp=" + timestamp +
                ", type=" + type +
                ", fever=" + fever +
                ", temperature=" + temperature +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        if (timestamp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(timestamp);
        }
        parcel.writeByte((byte) (fever ? 1 : 0));
        parcel.writeFloat(temperature);
        parcel.writeString(unit.name());
        parcel.writeString(type.name());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Temperature that = (Temperature) o;
        return fever == that.fever && Float.compare(that.temperature, temperature) == 0
                && unit == that.unit && Objects.equals(timestamp, that.timestamp)
                && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit, timestamp, type, fever, temperature);
    }
}

