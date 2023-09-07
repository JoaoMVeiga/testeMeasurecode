package com.pluxbiosignals.beurer.api.enums.glucometer;


import com.pluxbiosignals.beurer.api.interfaces.DeviceCommands;

import java.nio.ByteBuffer;

public enum GlucometerCommands implements DeviceCommands<byte[], Integer> {
    GET_ALL {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            return new byte[]{
                    GlucOPCodes.REPORT_STORED_RECORDS.code,
                    GlucOperatorCodes.OPERATOR_ALL_RECORDS.code
            };
        }
    },
    GET_FIRST {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            return new byte[]{
                    GlucOPCodes.REPORT_STORED_RECORDS.code,
                    GlucOperatorCodes.OPERATOR_FIRST_RECORD.code
            };
        }
    },
    GET_LAST {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            return new byte[]{
                    GlucOPCodes.REPORT_STORED_RECORDS.code,
                    GlucOperatorCodes.OPERATOR_LAST_RECORD.code
            };
        }
    },
    GET_GREATER_THAN {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            byte[] lowLimit = ByteBuffer.allocate(4).putInt(cmdArguments[0]).array();

            return new byte[]{
                    GlucOPCodes.REPORT_STORED_RECORDS.code,
                    GlucOperatorCodes.OPERATOR_GREATER_THAN_OR_EQUAL.code,
                    GlucOperatorFilterType.SEQUENCE_NUMBER.code,
                    lowLimit[3],
                    lowLimit[2]
            };
        }
    },
    GET_LESS_THAN {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            byte[] highLimit = ByteBuffer.allocate(4).putInt(cmdArguments[0]).array();

            return new byte[]{
                    GlucOPCodes.REPORT_STORED_RECORDS.code,
                    GlucOperatorCodes.OPERATOR_LESS_THAN_OR_EQUAL.code,
                    GlucOperatorFilterType.SEQUENCE_NUMBER.code,
                    highLimit[3],
                    highLimit[2]
            };
        }
    },
    GET_IN_RANGE {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            byte[] lowLimit = ByteBuffer.allocate(4).putInt(cmdArguments[0]).array();
            byte[] highLimit = ByteBuffer.allocate(4).putInt(cmdArguments[1]).array();

            return new byte[]{
                    GlucOPCodes.REPORT_STORED_RECORDS.code,
                    GlucOperatorCodes.OPERATOR_WITHIN_RANGE.code,
                    GlucOperatorFilterType.SEQUENCE_NUMBER.code,
                    lowLimit[3],
                    lowLimit[2],
                    highLimit[3],
                    highLimit[2]

            };
        }
    },
    GET_TOTAL {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            return new byte[]{
                    GlucOPCodes.REPORT_NUMBER_OF_RECORDS.code,
                    GlucOperatorCodes.OPERATOR_ALL_RECORDS.code
            };
        }
    },
    ABORT {
        @Override
        public byte[] getCommand(Integer... cmdArguments) {
            return new byte[]{
                    GlucOPCodes.ABORT_OPERATION.code,
                    GlucOperatorCodes.OPERATOR_NULL.code
            };
        }
    }
}



