/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums.scale;

import com.pluxbiosignals.beurer.api.interfaces.DeviceCommands;
import com.pluxbiosignals.beurer.api.utils.CRCUtils;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;

public enum WeightScaleCommands implements DeviceCommands<byte[], Integer> {
    SET_TIME {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            final LocalDateTime now = LocalDateTime.now();

            return new byte[]{
                    (byte) ((now.getYear()) & 0xFF),
                    (byte) (((now.getYear()) >> 8) & 0xFF),
                    (byte) ((now.getMonthValue()) & 0xFF),
                    (byte) (now.getDayOfMonth() & 0x1F),
                    (byte) (now.getHour() & 0x1F),
                    (byte) (now.getMinute() & 0x3F),
                    (byte) (now.getSecond() & 0x3F),
                    0x00,
                    0x00,
                    0x00
            };
        }
    },
    LIST_USERS {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            return new byte[]{
                    (byte) 0x00
            };
        }
    },
    TAKE_MEASURE {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            return new byte[]{
                    (byte) 0x00
            };
        }
    },
    CREATE_USER {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            byte[] consentCode = ByteBuffer.allocate(4).putInt(cmdArguments[0]).array();

            return new byte[]{
                    WeightScaleOPCodes.CREATE_USER.code,
                    consentCode[3],
                    consentCode[2]
            };
        }
    },
    CONSENT {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            byte[] userIndex = ByteBuffer.allocate(4).putInt(cmdArguments[0]).array();
            byte[] consentCode = ByteBuffer.allocate(4).putInt(cmdArguments[1]).array();

            return new byte[]{
                    WeightScaleOPCodes.CONSENT_PROCEDURE.code,
                    userIndex[3],
                    consentCode[3],
                    consentCode[2]
            };
        }
    },
    DELETE_USER_DATA {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            return new byte[]{
                    WeightScaleOPCodes.DELETE_USER_DATA.code,
            };
        }
    },
    SET_USER_DOB {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            byte[] year = ByteBuffer.allocate(4).putInt(cmdArguments[0]).array();
            byte[] month = ByteBuffer.allocate(4).putInt(cmdArguments[1]).array();
            byte[] day = ByteBuffer.allocate(4).putInt(cmdArguments[2]).array();

            return new byte[]{
                    year[3],
                    year[2],
                    month[3],
                    day[3],
            };
        }
    },
    SET_USER_GENDER {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            byte[] gender = ByteBuffer.allocate(4).putInt(cmdArguments[0]).array();

            return new byte[]{
                    gender[3]
            };
        }
    },
    SET_USER_HEIGHT {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            byte[] height = ByteBuffer.allocate(4).putInt(cmdArguments[0]).array();

            return new byte[]{
                    height[3]
            };
        }
    },
    SET_USER_ACTIVITY {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            byte[] activity = ByteBuffer.allocate(4).putInt(cmdArguments[0]).array();

            return new byte[]{
                    activity[3]
            };
        }
    },
}




