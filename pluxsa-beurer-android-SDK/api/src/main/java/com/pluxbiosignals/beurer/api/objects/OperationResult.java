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

public class OperationResult<T> implements Parcelable {
    private T operation;
    private final boolean success;

    public OperationResult(T operation, boolean success) {
        this.operation = operation;
        this.success = success;
    }

    protected OperationResult(T operation, Parcel in) {
        this.operation = operation;
        success = in.readByte() != 0;
    }


    protected OperationResult(Parcel in) {
        success = in.readByte() != 0;
    }

    public static final Creator<OperationResult<?>> CREATOR = new Creator<OperationResult<?>>() {
        @Override
        public OperationResult<?> createFromParcel(Parcel in) {
            return new OperationResult<>(in);
        }

        @Override
        public OperationResult<?>[] newArray(int size) {
            return new OperationResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeByte((byte) (success ? 1 : 0));
    }

    public T getOperation() {
        return operation;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "OperationResult{" +
                "operation=" + operation +
                ", success=" + success +
                '}';
    }
}
