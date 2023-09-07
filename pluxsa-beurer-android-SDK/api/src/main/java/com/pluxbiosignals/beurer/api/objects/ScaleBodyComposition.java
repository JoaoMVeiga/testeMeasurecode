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

import com.pluxbiosignals.beurer.api.enums.scale.BodyCompositionFlag;
import com.pluxbiosignals.beurer.api.enums.scale.ScaleUnit;

import java.util.Calendar;
import java.util.Objects;

public class ScaleBodyComposition implements Parcelable {

    private final ScaleUnit unit;
    private final float bodyFat;
    private final Long timestamp;
    private final Integer userId;
    private final Float basalMetabolism;
    private final Float musclePercentage;
    private final Float muscleMass;
    private final Float fatFreeMass;
    private final Float softLeanMass;
    private final Float bodyWaterMass;
    private final Float impedance;
    private final Float weight;
    private final Float height;


    public ScaleBodyComposition(ScaleUnit unit, float bodyFat, Long timestamp, Integer userId,
                                Float basalMetabolism, Float musclePercentage, Float muscleMass,
                                Float fatFreeMass, Float softLeanMass, Float bodyWaterMass,
                                Float impedance, Float weight, Float height) {
        this.unit = unit;
        this.bodyFat = bodyFat;
        this.timestamp = timestamp;
        this.userId = userId;
        this.basalMetabolism = basalMetabolism;
        this.musclePercentage = musclePercentage;
        this.muscleMass = muscleMass;
        this.fatFreeMass = fatFreeMass;
        this.softLeanMass = softLeanMass;
        this.bodyWaterMass = bodyWaterMass;
        this.impedance = impedance;
        this.weight = weight;
        this.height = height;
    }

    protected ScaleBodyComposition(Parcel in) {
        unit = ScaleUnit.valueOf(in.readString());
        bodyFat = in.readFloat();
        if (in.readByte() == 0) {
            timestamp = null;
        } else {
            timestamp = in.readLong();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        if (in.readByte() == 0) {
            basalMetabolism = null;
        } else {
            basalMetabolism = in.readFloat();
        }
        if (in.readByte() == 0) {
            musclePercentage = null;
        } else {
            musclePercentage = in.readFloat();
        }
        if (in.readByte() == 0) {
            muscleMass = null;
        } else {
            muscleMass = in.readFloat();
        }
        if (in.readByte() == 0) {
            fatFreeMass = null;
        } else {
            fatFreeMass = in.readFloat();
        }
        if (in.readByte() == 0) {
            softLeanMass = null;
        } else {
            softLeanMass = in.readFloat();
        }
        if (in.readByte() == 0) {
            bodyWaterMass = null;
        } else {
            bodyWaterMass = in.readFloat();
        }
        if (in.readByte() == 0) {
            impedance = null;
        } else {
            impedance = in.readFloat();
        }
        if (in.readByte() == 0) {
            weight = null;
        } else {
            weight = in.readFloat();
        }
        if (in.readByte() == 0) {
            height = null;
        } else {
            height = in.readFloat();
        }
    }

    public static final Creator<ScaleBodyComposition> CREATOR = new Creator<ScaleBodyComposition>() {
        @Override
        public ScaleBodyComposition createFromParcel(Parcel in) {
            return new ScaleBodyComposition(in);
        }

        @Override
        public ScaleBodyComposition[] newArray(int size) {
            return new ScaleBodyComposition[size];
        }
    };

    public static ScaleBodyComposition parseReply(byte[] data) {
        // Flag processing
        int flag = ((data[1] & 0xFF) << 8) | (data[0] & 0xFF);
        final ScaleUnit unit = ScaleUnit.getUnit(BodyCompositionFlag.UNIT.getValue() & flag);
        boolean hasTimestamp = (BodyCompositionFlag.TIMESTAMP.getValue() & flag) != 0;
        boolean hasUserId = (BodyCompositionFlag.USER_ID.getValue() & flag) != 0;
        boolean hasBasalMetabolism = (BodyCompositionFlag.BASAL_METABOLISM.getValue() & flag) != 0;
        boolean hasMusclePercentage = (BodyCompositionFlag.MUSCLE_PERCENTAGE.getValue() & flag) != 0;
        boolean hasMuscleMass = (BodyCompositionFlag.MUSCLE_MASS.getValue() & flag) != 0;
        boolean hasFatFree = (BodyCompositionFlag.FAT_FREE_MASS.getValue() & flag) != 0;
        boolean hasSoftLeanMass = (BodyCompositionFlag.SOFT_LEAN_MASS.getValue() & flag) != 0;
        boolean hasBodyWaterMass = (BodyCompositionFlag.BODY_WATER_MASS.getValue() & flag) != 0;
        boolean hasImpedance = (BodyCompositionFlag.IMPEDANCE.getValue() & flag) != 0;
        boolean hasWeight = (BodyCompositionFlag.WEIGHT.getValue() & flag) != 0;
        boolean hasHeight = (BodyCompositionFlag.HEIGHT.getValue() & flag) != 0;

        final float multiplier = unit.equals(ScaleUnit.SI) ? 0.005f : 0.01f;

        // Body Fat
        int bodyFatReceived = ((data[3] & 0xFF) << 8) | (data[2] & 0xFF);
        float bodyFat = (float) bodyFatReceived / 10f;

        // Timestamp
        int offset = 4;
        Long timestamp = null;
        if (hasTimestamp) {
            int year = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            byte month = (byte) (data[offset + 2] & 0xFF);
            byte day = (byte) (data[offset + 3] & 0xFF);
            byte hour = (byte) (data[offset + 4] & 0xFF);
            byte minute = (byte) (data[offset + 5] & 0xFF);
            byte second = (byte) (data[offset + 6] & 0xFF);

            final Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day, hour, minute, second);
            timestamp = calendar.getTimeInMillis() / 1000 * 1000;
            offset += 7;
        }

        //User ID
        Integer userId = null;
        if (hasUserId) {
            userId = data[offset] & 0xFF;
            offset += 1;
        }

        //BMR
        Float basalMetabolism = null;
        if (hasBasalMetabolism) {
            short bmrInJoules = (short) (((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF));
            basalMetabolism = ((bmrInJoules / 4184f));//in kcal
            offset += 2;
        }

        //muscle percentage
        Float musclePercentage = null;
        if (hasMusclePercentage) {
            short musclePercentageReceived = (short) (((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF));
            musclePercentage = (float) musclePercentageReceived / 10f;

            offset += 2;
        }

        //muscle mass
        Float muscleMass = null;
        if (hasMuscleMass) {
            short muscleMassReceived = (short) (((data[offset + 1] & 0xFFFF) << 8) | (data[offset] & 0xFF));
            muscleMass = muscleMassReceived * multiplier;

            offset += 2;
        }

        //fat free mass
        Float fatFreeMass = null;
        if (hasFatFree) {
            short fatFreeMassReceived = (short) (((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF));
            fatFreeMass = fatFreeMassReceived * multiplier;

            offset += 2;
        }

        //soft lean mass
        Float softLeanMass = null;
        if (hasSoftLeanMass) {
            short softLeanMassReceived = (short) (((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF));
            softLeanMass = softLeanMassReceived * multiplier;

            offset += 2;
        }

        //body water mass
        Float bodyWaterMass = null;
        if (hasBodyWaterMass) {
            short bodyWaterMassReceived = (short) (((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF));
            bodyWaterMass = bodyWaterMassReceived * multiplier;

            offset += 2;
        }

        Float impedance = null;
        if (hasImpedance) {
            short impedanceReceived = (short) (((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF));
            impedance = (float) impedanceReceived / 10f;

            offset += 2;
        }

        Float weight = null;
        if (hasWeight) {
            short weightReceived = (short) (((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF));
            weight = weightReceived * multiplier;

            offset += 2;
        }

        Float height = null;
        if (hasHeight) {
            short heightReceived = (short) (((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF));
            height = unit.equals(ScaleUnit.SI) ? (heightReceived / 1000f)
                    : (float) heightReceived / 10f;
        }

        return new ScaleBodyComposition(unit, bodyFat, timestamp, userId, basalMetabolism,
                musclePercentage, muscleMass, fatFreeMass, softLeanMass, bodyWaterMass, impedance,
                weight, height);
    }

    public ScaleUnit getUnit() {
        return unit;
    }

    public float getBodyFat() {
        return bodyFat;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Integer getUserId() {
        return userId;
    }

    public Float getBasalMetabolism() {
        return basalMetabolism;
    }

    public Float getMusclePercentage() {
        return musclePercentage;
    }

    public Float getMuscleMass() {
        return muscleMass;
    }

    public Float getFatFreeMass() {
        return fatFreeMass;
    }

    public Float getSoftLeanMass() {
        return softLeanMass;
    }

    public Float getBodyWaterMass() {
        return bodyWaterMass;
    }

    public Float getImpedance() {
        return impedance;
    }

    public Float getWeight() {
        return weight;
    }

    public Float getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "ScaleBodyComposition{" +
                "unit=" + unit +
                ", bodyFat=" + bodyFat +
                ", timestamp=" + timestamp +
                ", userId=" + userId +
                ", basalMetabolism=" + basalMetabolism +
                ", musclePercentage=" + musclePercentage +
                ", muscleMass=" + muscleMass +
                ", fatFreeMass=" + fatFreeMass +
                ", softLeanMass=" + softLeanMass +
                ", bodyWaterMass=" + bodyWaterMass +
                ", impedance=" + impedance +
                ", weight=" + weight +
                ", height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScaleBodyComposition that = (ScaleBodyComposition) o;
        return Float.compare(that.bodyFat, bodyFat) == 0 && unit == that.unit
                && Objects.equals(timestamp, that.timestamp)
                && Objects.equals(userId, that.userId)
                && Objects.equals(basalMetabolism, that.basalMetabolism)
                && Objects.equals(musclePercentage, that.musclePercentage)
                && Objects.equals(muscleMass, that.muscleMass)
                && Objects.equals(fatFreeMass, that.fatFreeMass)
                && Objects.equals(softLeanMass, that.softLeanMass)
                && Objects.equals(bodyWaterMass, that.bodyWaterMass)
                && Objects.equals(impedance, that.impedance)
                && Objects.equals(weight, that.weight)
                && Objects.equals(height, that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit, bodyFat, timestamp, userId, basalMetabolism, musclePercentage,
                muscleMass, fatFreeMass, softLeanMass, bodyWaterMass, impedance, weight, height);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(unit.name());
        parcel.writeFloat(bodyFat);
        if (timestamp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(timestamp);
        }
        if (userId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(userId);
        }
        if (basalMetabolism == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(basalMetabolism);
        }
        if (musclePercentage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(musclePercentage);
        }
        if (muscleMass == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(muscleMass);
        }
        if (fatFreeMass == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(fatFreeMass);
        }
        if (softLeanMass == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(softLeanMass);
        }
        if (bodyWaterMass == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(bodyWaterMass);
        }
        if (impedance == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(impedance);
        }
        if (weight == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(weight);
        }
        if (height == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(height);
        }
    }
}
