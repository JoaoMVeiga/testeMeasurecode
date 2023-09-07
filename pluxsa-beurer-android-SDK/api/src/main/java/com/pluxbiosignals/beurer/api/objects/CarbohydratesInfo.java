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

import com.pluxbiosignals.beurer.api.enums.glucometer.GlucCarbohydrateId;

import java.util.Objects;

public class CarbohydratesInfo implements Parcelable {

    private final GlucCarbohydrateId carbohydrateId;
    private final float quantity;

    public CarbohydratesInfo(GlucCarbohydrateId carbohydrateId, float quantity) {
        this.carbohydrateId = carbohydrateId;
        this.quantity = quantity;
    }

    protected CarbohydratesInfo(Parcel in) {
        carbohydrateId = GlucCarbohydrateId.valueOf(in.readString());
        quantity = in.readFloat();
    }

    public static final Creator<CarbohydratesInfo> CREATOR = new Creator<CarbohydratesInfo>() {
        @Override
        public CarbohydratesInfo createFromParcel(Parcel in) {
            return new CarbohydratesInfo(in);
        }

        @Override
        public CarbohydratesInfo[] newArray(int size) {
            return new CarbohydratesInfo[size];
        }
    };

    public GlucCarbohydrateId getCarbohydrateId() {
        return carbohydrateId;
    }

    public float getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "CarbohydratesInfo{" +
                "carbohydrateId=" + carbohydrateId +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarbohydratesInfo that = (CarbohydratesInfo) o;
        return Float.compare(that.quantity, quantity) == 0 && carbohydrateId == that.carbohydrateId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(carbohydrateId, quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(carbohydrateId.name());
        parcel.writeFloat(quantity);
    }
}
