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

import com.pluxbiosignals.beurer.api.enums.bpm.BpmStatusFeature;
import com.pluxbiosignals.beurer.api.enums.bpm.Hsd;
import com.pluxbiosignals.beurer.api.enums.bpm.PulseRange;

import java.util.Objects;

public class BpmMeasureStatus implements Parcelable {
    //Status
    private final boolean bodyMovement;
    private final boolean properFit;
    private final boolean irregularPulse;
    private final PulseRange range;
    private final boolean properPosition;
    private final Hsd hsd;

    public BpmMeasureStatus(boolean bodyMovement, boolean properFit, boolean irregularPulse,
                            PulseRange range, boolean properPosition, Hsd hsd) {
        this.bodyMovement = bodyMovement;
        this.properFit = properFit;
        this.irregularPulse = irregularPulse;
        this.range = range;
        this.properPosition = properPosition;
        this.hsd = hsd;
    }

    protected BpmMeasureStatus(Parcel in) {
        bodyMovement = in.readByte() != 0;
        properFit = in.readByte() != 0;
        irregularPulse = in.readByte() != 0;
        range = PulseRange.valueOf(in.readString());
        properPosition = in.readByte() != 0;
        hsd = Hsd.valueOf(in.readString());
    }

    public static final Creator<BpmMeasureStatus> CREATOR = new Creator<BpmMeasureStatus>() {
        @Override
        public BpmMeasureStatus createFromParcel(Parcel in) {
            return new BpmMeasureStatus(in);
        }

        @Override
        public BpmMeasureStatus[] newArray(int size) {
            return new BpmMeasureStatus[size];
        }
    };

    public static BpmMeasureStatus parseReply(short measurementStatus, boolean hasHsd) throws Exception {
        boolean bodyMovement = (BpmStatusFeature.BODY_MOVEMENT_DETECTION.getValue() & measurementStatus) != 0;
        boolean properFit = (BpmStatusFeature.CUFF_FIT_DETECTION.getValue() & measurementStatus) == 0;
        boolean irregularPulseDetection = (BpmStatusFeature.IRREGULAR_PULSE_DETECTION.getValue() & measurementStatus) != 0;
        PulseRange pulseRateRangeDetection = PulseRange.getPulseRange((byte) ((BpmStatusFeature
                .PULSE_RATE_RANGE_DETECTION.getValue() & measurementStatus) >> 3));
        boolean properPosition = (BpmStatusFeature.MEASUREMENT_POSITION_DETECTION.getValue() & measurementStatus) == 0;

        Hsd hsd = Hsd.UNKNOWN;
        if (hasHsd) {
            hsd = Hsd.getHsd((byte) ((BpmStatusFeature.HSD_DETECTION.getValue() & measurementStatus) >> 6));
        }

        return new BpmMeasureStatus(bodyMovement, properFit, irregularPulseDetection,
                pulseRateRangeDetection, properPosition, hsd);
    }

    public boolean isBodyMovement() {
        return bodyMovement;
    }

    public boolean isProperFit() {
        return properFit;
    }

    public boolean isIrregularPulse() {
        return irregularPulse;
    }

    public PulseRange getRange() {
        return range;
    }

    public boolean isProperPosition() {
        return properPosition;
    }

    public Hsd getHsd() {
        return hsd;
    }

    @Override
    public String toString() {
        return "BpmMeasureStatus{" +
                "bodyMovement=" + bodyMovement +
                ", properFit=" + properFit +
                ", irregularPulse=" + irregularPulse +
                ", range=" + range +
                ", properPosition=" + properPosition +
                ", hsd=" + hsd +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BpmMeasureStatus that = (BpmMeasureStatus) o;
        return bodyMovement == that.bodyMovement && properFit == that.properFit
                && irregularPulse == that.irregularPulse
                && properPosition == that.properPosition && range == that.range && hsd == that.hsd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bodyMovement, properFit, irregularPulse, range, properPosition, hsd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeByte((byte) (bodyMovement ? 1 : 0));
        parcel.writeByte((byte) (properFit ? 1 : 0));
        parcel.writeByte((byte) (irregularPulse ? 1 : 0));
        parcel.writeString(range.name());
        parcel.writeByte((byte) (properPosition ? 1 : 0));
        parcel.writeString(hsd.name());
    }
}
