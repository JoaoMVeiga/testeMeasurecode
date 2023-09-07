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

import com.pluxbiosignals.beurer.api.enums.scale.Gender;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class ScaleUser {
    private Integer userIndex;
    private final long dateOfBirth;

    private final int height;// in centimeters
    private final Gender gender;
    private final int activityLevel;

    public ScaleUser(Integer userIndex, long dateOfBirth, int height, Gender gender, int activityLevel) {
        this.userIndex = userIndex;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.gender = gender;
        this.activityLevel = activityLevel;
    }

    public ScaleUser(long dateOfBirth, int height, Gender gender, int activityLevel) {
        this.userIndex = null;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.gender = gender;
        this.activityLevel = activityLevel;
    }


    public static ScaleUser parseReply(byte[] receivedData) {
        int userIndex = receivedData[1];

        int offset = 5;

        int year = (((receivedData[offset + 1] & 0xFF) << 8) | (receivedData[offset] & 0xFF));
        int month = receivedData[offset + 2] & 0xFF;
        int day = receivedData[offset + 3] & 0xFF;

        // Create a LocalDateTime object using the received values
        final LocalDateTime localDateTime = LocalDateTime.of(year, month, day, 0, 0, 0);

        // Convert LocalDateTime to a Unix timestamp
        final long timestamp = localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000L;

        int height = receivedData[offset + 4] & 0xFF;
        int gender = receivedData[offset + 5] & 0xFF;
        int activityLevel = receivedData[offset + 6] & 0xFF;

        return new ScaleUser(userIndex, timestamp, height, Gender.values()[gender], activityLevel);
    }

    public Integer getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(Integer userIndex) {
        this.userIndex = userIndex;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public int getHeight() {
        return height;
    }

    public Gender getGender() {
        return gender;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    @Override
    public String toString() {
        return "ScaleUser{" +
                "userIndex=" + userIndex +
                ", dateOfBirth=" + dateOfBirth +
                ", height=" + height +
                ", gender=" + gender +
                ", activityLevel=" + activityLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScaleUser scaleUser = (ScaleUser) o;
        return dateOfBirth == scaleUser.dateOfBirth && height == scaleUser.height
                && gender == scaleUser.gender && activityLevel == scaleUser.activityLevel
                && Objects.equals(userIndex, scaleUser.userIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIndex, dateOfBirth, height, gender, activityLevel);
    }
}
