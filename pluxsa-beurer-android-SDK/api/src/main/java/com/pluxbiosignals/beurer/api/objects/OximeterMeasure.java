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

import java.util.Calendar;
import java.util.Objects;

public class OximeterMeasure implements Parcelable {
    private final long startTime;
    private final long endTime;
    private final int period;
    private final int spo2Max;
    private final int spo2Min;
    private final int spo2Avg;
    private final int prMax;
    private final int prMin;
    private final int prAvg;

    public OximeterMeasure(long startTime, long endTime, int period, int spo2Max,
                           int spo2Min, int spo2Avg, int prMax, int prMin, int prAvg) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.period = period;
        this.spo2Max = spo2Max;
        this.spo2Min = spo2Min;
        this.spo2Avg = spo2Avg;
        this.prMax = prMax;
        this.prMin = prMin;
        this.prAvg = prAvg;
    }

    protected OximeterMeasure(Parcel in) {
        startTime = in.readLong();
        endTime = in.readLong();
        period = in.readInt();
        spo2Max = in.readInt();
        spo2Min = in.readInt();
        spo2Avg = in.readInt();
        prMax = in.readInt();
        prMin = in.readInt();
        prAvg = in.readInt();
    }

    public static final Creator<OximeterMeasure> CREATOR = new Creator<OximeterMeasure>() {
        @Override
        public OximeterMeasure createFromParcel(Parcel in) {
            return new OximeterMeasure(in);
        }

        @Override
        public OximeterMeasure[] newArray(int size) {
            return new OximeterMeasure[size];
        }
    };

    public static OximeterMeasure parseReply(byte[] receivedData) throws Exception {
        boolean crcOk = checkPulseOxiChecksum(receivedData);
        if (!crcOk) {
            throw new Exception("Checksum not OK.");
        }

        //Start time
        int year = (receivedData[2] & 0x7F) + 2000;
        int month = receivedData[3] & 0x0F;
        int day = receivedData[4] & 0x1F;
        int hour = receivedData[5] & 0x1F;
        int minute = receivedData[6] & 0x3F;
        int second = receivedData[7] & 0x3F;
        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, month - 1, day, hour, minute, second);
        long startTime = startCalendar.getTimeInMillis() / 1000 * 1000;

        //End time
        year = (receivedData[8] & 0x7F) + 2000;
        month = receivedData[9] & 0x0F;
        day = receivedData[10] & 0x1F;
        hour = receivedData[11] & 0x1F;
        minute = receivedData[12] & 0x3F;
        second = receivedData[13] & 0x3F;
        final Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, month - 1, day, hour, minute, second);
        long endTime = endCalendar.getTimeInMillis() / 1000 * 1000;

        //Pulse rate
        byte prMaxMsb = (byte) ((receivedData[14] & 0x20) << 2);
        byte prMinMsb = (byte) ((receivedData[14] & 0x10) << 3);
        byte prAvgMsb = (byte) ((receivedData[14] & 0x8) << 4);
        byte prMaxLsb = (byte) (receivedData[20] & 0x7F);
        byte prMinLsb = (byte) (receivedData[21] & 0x7F);
        byte prAvgLsb = (byte) (receivedData[22] & 0x7F);
        int prMax = (prMaxLsb & 0xFF) | (prMaxMsb & 0xFF);
        int prMin = (prMinLsb & 0xFF) | (prMinMsb & 0xFF);
        int prAvg = (prAvgLsb & 0xFF) | (prAvgMsb & 0xFF);

        //storage period
        int storagePeriodMsb = (receivedData[14] & 0x7) << 14;
        int storagePeriodLsb = ((receivedData[15] & 0xFF) << 8) | (receivedData[16] & 0xFF);
        int storagePeriod = storagePeriodMsb | storagePeriodLsb;

        //spo2
        byte spo2Max = (byte) (receivedData[17] & 0x7F);
        byte spo2Min = (byte) (receivedData[18] & 0x7F);
        byte spo2Avg = (byte) (receivedData[19] & 0x7F);

        return new OximeterMeasure(startTime, endTime,
                storagePeriod, spo2Max, spo2Min, spo2Avg, prMax, prMin, prAvg);
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getPeriod() {
        return period;
    }

    public int getSpo2Max() {
        return spo2Max;
    }

    public int getSpo2Min() {
        return spo2Min;
    }

    public int getSpo2Avg() {
        return spo2Avg;
    }

    public int getPrMax() {
        return prMax;
    }

    public int getPrMin() {
        return prMin;
    }

    public int getPrAvg() {
        return prAvg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OximeterMeasure that = (OximeterMeasure) o;
        return startTime == that.startTime && endTime == that.endTime
                && period == that.period && spo2Max == that.spo2Max && spo2Min == that.spo2Min
                && spo2Avg == that.spo2Avg && prMax == that.prMax && prMin == that.prMin
                && prAvg == that.prAvg;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, period, spo2Max, spo2Min, spo2Avg, prMax,
                prMin, prAvg);
    }

    @Override
    public String toString() {
        return "OximeterMeasure{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", period=" + period +
                ", spo2Max=" + spo2Max +
                ", spo2Min=" + spo2Min +
                ", spo2Avg=" + spo2Avg +
                ", prMax=" + prMax +
                ", prMin=" + prMin +
                ", prAvg=" + prAvg +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(startTime);
        parcel.writeLong(endTime);
        parcel.writeInt(period);
        parcel.writeInt(spo2Max);
        parcel.writeInt(spo2Min);
        parcel.writeInt(spo2Avg);
        parcel.writeInt(prMax);
        parcel.writeInt(prMin);
        parcel.writeInt(prAvg);
    }
}
