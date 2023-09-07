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

import static com.pluxbiosignals.beurer.api.utils.CRCUtils.checkPulseOxiChecksum;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class OximeterTime implements Parcelable {

    private final long oximeterTime;//seconds

    public OximeterTime(long oximeterTime) {
        this.oximeterTime = oximeterTime;
    }

    protected OximeterTime(Parcel in) {
        oximeterTime = in.readLong();
    }

    public static final Creator<OximeterTime> CREATOR = new Creator<OximeterTime>() {
        @Override
        public OximeterTime createFromParcel(Parcel in) {
            return new OximeterTime(in);
        }

        @Override
        public OximeterTime[] newArray(int size) {
            return new OximeterTime[size];
        }
    };

    @SuppressLint("DefaultLocale")
    public static OximeterTime parseReply(byte[] receivedData) throws Exception {
        boolean crcOk = checkPulseOxiChecksum(receivedData);
        if (!crcOk) {
            throw new Exception("Checksum not OK.");
        }

        int year = receivedData[1] + 2000;//Assuming that value represents years starting from 2000
        int month = receivedData[2];
        int day = receivedData[3];
        int hour = receivedData[4];
        int minute = receivedData[5];
        int second = receivedData[6];

        // Create a LocalDateTime object using the received values
        final LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);

        // Convert LocalDateTime to a Unix timestamp
        final long timestamp = localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000L;

        return new OximeterTime(timestamp);
    }

    public long getOximeterTime() {
        return oximeterTime;
    }

    @Override
    public String toString() {
        return "OximeterTime{" +
                "oximeterTime=" + oximeterTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(oximeterTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OximeterTime that = (OximeterTime) o;
        return oximeterTime == that.oximeterTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(oximeterTime);
    }
}
