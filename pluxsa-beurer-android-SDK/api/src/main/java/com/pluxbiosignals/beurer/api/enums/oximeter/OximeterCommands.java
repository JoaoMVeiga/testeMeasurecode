/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.enums.oximeter;

import com.pluxbiosignals.beurer.api.interfaces.DeviceCommands;
import com.pluxbiosignals.beurer.api.utils.CRCUtils;

import java.time.LocalDateTime;

public enum OximeterCommands implements DeviceCommands<byte[], Byte> {
    GET_VERSION {
        @Override
        public byte[] getCommand(Byte[] cmdArguments) {
            byte[] message = new byte[]{(byte) 0x82};
            return makeCommand(message);
        }
    },
    SET_TIME_SYNCHRONIZATION {
        @Override
        public byte[] getCommand(Byte[] cmdArguments) {
            LocalDateTime now = LocalDateTime.now();


            byte[] message = new byte[]{
                    (byte) 0x83,
                    (byte) ((now.getYear() - 2000) & 0x7F),
                    (byte) (now.getMonthValue() & 0x0F),
                    (byte) (now.getDayOfMonth() & 0x1F),
                    (byte) (now.getHour() & 0x1F),
                    (byte) (now.getMinute() & 0x3F),
                    (byte) (now.getSecond() & 0x3F),
                    (byte) 0x00,
                    (byte) 0x00
            };
            return makeCommand(message);
        }
    },
    GET_TIME {
        @Override
        public byte[] getCommand(Byte[] cmdArguments) {
            byte[] message = new byte[]{(byte) 0x89};
            return makeCommand(message);
        }
    },
    GET_DATA_STORAGE_INFO {
        @Override
        public byte[] getCommand(Byte[] cmdArguments) {
            byte[] message = new byte[]{(byte) 0x90, (byte) 0x05};
            return makeCommand(message);
        }
    },
    GET_MEASUREMENT_DATA {
        @Override
        public byte[] getCommand(Byte[] cmdArguments) {
            byte[] message = new byte[]{
                    (byte) 0x99,
                    (byte) cmdArguments[0]
            };
            return makeCommand(message);
        }
    };

    static byte[] makeCommand(byte[] message) {
        byte[] command = new byte[message.length + 1]; //one extra byte for CRC
        byte crc = CRCUtils.pulseOximeterCRC(message);

        System.arraycopy(message, 0, command, 0, message.length);
        command[command.length - 1] = crc;
        return command;
    }
}




