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
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucContextFlag;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucMeal;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucMedId;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucMedsUnit;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucTester;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucTesterHealth;
import com.pluxbiosignals.beurer.api.utils.ConversionUtils;

import java.util.Objects;

public class GlucContext implements Parcelable {

    private final int nSeq;
    private final CarbohydratesInfo carbohydrates;
    private final GlucMeal meal;
    private final GlucTester tester;
    private final GlucTesterHealth testerHealth;
    private final ExerciseInfo exerciseInfo;
    private final MedInfo medInfo;
    private final Float hbA1c;

    public GlucContext(int nSeq, CarbohydratesInfo carbohydrates, GlucMeal meal, GlucTester tester,
                       GlucTesterHealth testerHealth, ExerciseInfo exerciseInfo, MedInfo medInfo,
                       Float hbA1c) {
        this.nSeq = nSeq;
        this.carbohydrates = carbohydrates;
        this.meal = meal;
        this.tester = tester;
        this.testerHealth = testerHealth;
        this.exerciseInfo = exerciseInfo;
        this.medInfo = medInfo;
        this.hbA1c = hbA1c;
    }

    protected GlucContext(Parcel in) {
        nSeq = in.readInt();
        carbohydrates = in.readParcelable(CarbohydratesInfo.class.getClassLoader());
        meal = GlucMeal.valueOf(in.readString());
        tester = GlucTester.valueOf(in.readString());
        testerHealth = GlucTesterHealth.valueOf(in.readString());
        exerciseInfo = in.readParcelable(ExerciseInfo.class.getClassLoader());
        medInfo = in.readParcelable(MedInfo.class.getClassLoader());
        if (in.readByte() == 0) {
            hbA1c = null;
        } else {
            hbA1c = in.readFloat();
        }
    }

    public static final Creator<GlucContext> CREATOR = new Creator<GlucContext>() {
        @Override
        public GlucContext createFromParcel(Parcel in) {
            return new GlucContext(in);
        }

        @Override
        public GlucContext[] newArray(int size) {
            return new GlucContext[size];
        }
    };

    public static GlucContext parseReply(byte[] receivedData) throws Exception {
        // Flag processing
        byte flag = receivedData[0];
        boolean hasCarbohydratesField = (GlucContextFlag.CARBON_HYDRATES.getValue() & flag) != 0;
        boolean hasMealField = (GlucContextFlag.MEAL.getValue() & flag) != 0;
        boolean hasTesterHealthField = (GlucContextFlag.TESTER_HEALTH.getValue() & flag) != 0;
        boolean hasExerciseField = (GlucContextFlag.EXERCISE.getValue() & flag) != 0;
        boolean hasMedicationIDField = (GlucContextFlag.MEDICATION_ID.getValue() & flag) != 0;
        final GlucMedsUnit glucMedsUnit = GlucMedsUnit
                .getUnit(GlucContextFlag.MEDICATION_UNITS.getValue() & flag);
        boolean hasHbA1cField = (GlucContextFlag.HbA1c.getValue() & flag) != 0;
        boolean hasExtendedField = (GlucContextFlag.EXTENDED.getValue() & flag) != 0;

        // Sequence Number
        short nSeq = (short) ((receivedData[2] << 8) | (receivedData[1] & 0xFF));
        int offset = 3;
        if (hasExtendedField) {
            offset += 1;
        }

        CarbohydratesInfo carbohydratesInfo = null;
        if (hasCarbohydratesField) {
            byte carbohydrateID = receivedData[offset];
            float carbohydrateQuantity = ConversionUtils.sFloat((short) ((receivedData[offset + 1] << 8)
                    | (receivedData[offset + 2] & 0xFF)));
            carbohydratesInfo = new CarbohydratesInfo(GlucCarbohydrateId
                    .getCarbohydrateId(carbohydrateID), carbohydrateQuantity);

            offset += 3;
        }

        GlucMeal meal = GlucMeal.UNKNOWN;
        if (hasMealField) {
            byte receivedMeal = receivedData[offset];
            meal = GlucMeal.getMeal(receivedMeal);
            offset += 1;
        }

        GlucTester glucTester = GlucTester.TESTER_NOT_AVAILABLE;
        GlucTesterHealth testerHealth = GlucTesterHealth.HEALTH_NO;
        if (hasTesterHealthField) {
            byte testerData = receivedData[offset];
            byte tester = (byte) ((testerData & 0xF0) >> 4);
            byte health = (byte) (testerData & 0x0F);

            glucTester = GlucTester.getTester(tester);
            testerHealth = GlucTesterHealth.getTesterHealth(health);

            offset += 1;
        }

        ExerciseInfo exerciseInfo = null;
        if (hasExerciseField) {
            short exerciseDuration = (short) ((receivedData[offset] << 8)
                    | (receivedData[offset + 1] & 0xFF));
            byte exerciseIntensity = receivedData[offset + 2];

            exerciseInfo = new ExerciseInfo(exerciseDuration, exerciseIntensity);

            offset += 3;
        }

        MedInfo medInfo = null;
        if (hasMedicationIDField) {
            byte medicationId = receivedData[offset];
            float medicationQuantity = ConversionUtils
                    .sFloat((short) ((receivedData[offset + 1] << 8) | (receivedData[offset + 2] & 0xFF)));

            medInfo = new MedInfo(GlucMedId.getMedId(medicationId), medicationQuantity, glucMedsUnit);
            offset += 3;
        }

        Float hba1c = null;
        if (hasHbA1cField) {
            hba1c = ConversionUtils
                    .sFloat((short) ((receivedData[offset] << 8) | (receivedData[offset + 1] & 0xFF)));
        }

        return new GlucContext(nSeq, carbohydratesInfo, meal, glucTester, testerHealth,
                exerciseInfo, medInfo, hba1c);
    }

    public int getnSeq() {
        return nSeq;
    }

    public CarbohydratesInfo getCarbohydrates() {
        return carbohydrates;
    }

    public GlucMeal getMeal() {
        return meal;
    }

    public GlucTester getTester() {
        return tester;
    }

    public GlucTesterHealth getTesterHealth() {
        return testerHealth;
    }

    public ExerciseInfo getExerciseInfo() {
        return exerciseInfo;
    }

    public MedInfo getMedInfo() {
        return medInfo;
    }

    public Float getHbA1c() {
        return hbA1c;
    }

    @Override
    public String toString() {
        return "GlucContext{" +
                "nSeq=" + nSeq +
                ", carbohydrates=" + carbohydrates +
                ", meal=" + meal +
                ", tester=" + tester +
                ", testerHealth=" + testerHealth +
                ", exerciseInfo=" + exerciseInfo +
                ", medInfo=" + medInfo +
                ", hbA1c=" + hbA1c +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlucContext context = (GlucContext) o;
        return nSeq == context.nSeq && Objects.equals(carbohydrates, context.carbohydrates)
                && meal == context.meal && tester == context.tester
                && testerHealth == context.testerHealth
                && Objects.equals(exerciseInfo, context.exerciseInfo)
                && Objects.equals(medInfo, context.medInfo) && Objects.equals(hbA1c, context.hbA1c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nSeq, carbohydrates, meal, tester, testerHealth, exerciseInfo, medInfo,
                hbA1c);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(nSeq);
        parcel.writeParcelable(carbohydrates, i);
        parcel.writeString(meal.name());
        parcel.writeString(tester.name());
        parcel.writeString(testerHealth.name());
        parcel.writeParcelable(exerciseInfo, i);
        parcel.writeParcelable(medInfo, i);
        if (hbA1c == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(hbA1c);
        }
    }
}
