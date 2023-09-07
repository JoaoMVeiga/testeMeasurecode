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

import com.pluxbiosignals.beurer.api.enums.glucometer.GlucMedId;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucMedsUnit;

import java.util.Objects;

public class MedInfo implements Parcelable {
    private final GlucMedId medId;
    private final float medQuantity;

    private final GlucMedsUnit unit;

    public MedInfo(GlucMedId medId, float medQuantity, GlucMedsUnit glucMedsUnit) {
        this.medId = medId;
        this.medQuantity = medQuantity;
        this.unit = glucMedsUnit;
    }

    protected MedInfo(Parcel in) {
        medId = GlucMedId.valueOf(in.readString());
        medQuantity = in.readFloat();
        unit = GlucMedsUnit.valueOf(in.readString());
    }

    public static final Creator<MedInfo> CREATOR = new Creator<MedInfo>() {
        @Override
        public MedInfo createFromParcel(Parcel in) {
            return new MedInfo(in);
        }

        @Override
        public MedInfo[] newArray(int size) {
            return new MedInfo[size];
        }
    };

    public GlucMedId getMedId() {
        return medId;
    }

    public float getMedQuantity() {
        return medQuantity;
    }

    public GlucMedsUnit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "MedInfo{" +
                "medId=" + medId +
                ", medQuantity=" + medQuantity +
                ", unit=" + unit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedInfo medInfo = (MedInfo) o;
        return Float.compare(medInfo.medQuantity, medQuantity) == 0
                && medId == medInfo.medId && unit == medInfo.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(medId, medQuantity, unit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(medId.name());
        parcel.writeFloat(medQuantity);
        parcel.writeString(unit.name());
    }
}
