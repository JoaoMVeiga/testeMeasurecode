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

import java.util.Objects;

public class ExerciseInfo implements Parcelable {
    private final int duration;
    private final int intensity;

    public ExerciseInfo(int duration, int intensity) {
        this.duration = duration;
        this.intensity = intensity;
    }

    protected ExerciseInfo(Parcel in) {
        duration = in.readInt();
        intensity = in.readInt();
    }

    public static final Creator<ExerciseInfo> CREATOR = new Creator<ExerciseInfo>() {
        @Override
        public ExerciseInfo createFromParcel(Parcel in) {
            return new ExerciseInfo(in);
        }

        @Override
        public ExerciseInfo[] newArray(int size) {
            return new ExerciseInfo[size];
        }
    };

    public int getDuration() {
        return duration;
    }

    public int getIntensity() {
        return intensity;
    }

    @Override
    public String toString() {
        return "ExerciseInfo{" +
                "duration=" + duration +
                ", intensity=" + intensity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExerciseInfo that = (ExerciseInfo) o;
        return duration == that.duration && intensity == that.intensity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, intensity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(duration);
        parcel.writeInt(intensity);
    }
}
