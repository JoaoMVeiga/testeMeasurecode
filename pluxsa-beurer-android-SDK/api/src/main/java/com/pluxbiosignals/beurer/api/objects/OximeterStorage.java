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

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

public class OximeterStorage implements Parcelable {
    private final int totalCapacity;
    private final int totalUnread;

    public OximeterStorage(int totalCapacity, int totalUnread) {
        this.totalCapacity = totalCapacity;
        this.totalUnread = totalUnread;
    }

    protected OximeterStorage(Parcel in) {
        totalCapacity = in.readInt();
        totalUnread = in.readInt();
    }

    public static final Creator<OximeterStorage> CREATOR = new Creator<OximeterStorage>() {
        @Override
        public OximeterStorage createFromParcel(Parcel in) {
            return new OximeterStorage(in);
        }

        @Override
        public OximeterStorage[] newArray(int size) {
            return new OximeterStorage[size];
        }
    };

    public static OximeterStorage parseReply(byte[] receivedData) throws Exception {
        boolean crcOk = checkPulseOxiChecksum(receivedData);
        if (!crcOk) {
            throw new Exception("Checksum not OK.");
        }

        int totalCapacity = ByteBuffer.wrap(receivedData, 2, 2)
                .order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        int totalUnread = ByteBuffer.wrap(receivedData, 4, 2)
                .order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;

        return new OximeterStorage(totalCapacity, totalUnread);
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public int getTotalUnread() {
        return totalUnread;
    }

    @Override
    public String toString() {
        return "OximeterStorage{" +
                "totalCapacity=" + totalCapacity +
                ", totalUnread=" + totalUnread +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(totalCapacity);
        parcel.writeInt(totalUnread);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OximeterStorage that = (OximeterStorage) o;
        return totalCapacity == that.totalCapacity && totalUnread == that.totalUnread;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalCapacity, totalUnread);
    }
}
