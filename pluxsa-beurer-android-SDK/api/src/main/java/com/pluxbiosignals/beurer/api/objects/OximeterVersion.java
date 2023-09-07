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

import java.util.Objects;

public class OximeterVersion implements Parcelable {
    private final String hwVersion;
    private final String swVersion;

    public OximeterVersion(String hwVersion, String swVersion) {
        this.hwVersion = hwVersion;
        this.swVersion = swVersion;
    }

    protected OximeterVersion(Parcel in) {
        hwVersion = in.readString();
        swVersion = in.readString();
    }

    public static final Creator<OximeterVersion> CREATOR = new Creator<OximeterVersion>() {
        @Override
        public OximeterVersion createFromParcel(Parcel in) {
            return new OximeterVersion(in);
        }

        @Override
        public OximeterVersion[] newArray(int size) {
            return new OximeterVersion[size];
        }
    };

    @SuppressLint("DefaultLocale")
    public static OximeterVersion parseReply(byte[] receivedData) throws Exception {
        boolean crcOk = checkPulseOxiChecksum(receivedData);
        if (!crcOk) {
            throw new Exception("Checksum not OK.");
        }

        String hw = String.format("%d.%d.%d", receivedData[3], receivedData[2], receivedData[1]);
        String sw = String.format("%d.%d.%d", receivedData[6], receivedData[5], receivedData[4]);

        return new OximeterVersion(hw, sw);
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public String getSwVersion() {
        return swVersion;
    }

    @Override
    public String toString() {
        return "OximeterVersion{" +
                "hwVersion='" + hwVersion + '\'' +
                ", swVersion='" + swVersion + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(hwVersion);
        parcel.writeString(swVersion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OximeterVersion version = (OximeterVersion) o;
        return Objects.equals(hwVersion, version.hwVersion) && Objects.equals(swVersion, version.swVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hwVersion, swVersion);
    }
}
