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

import com.pluxbiosignals.beurer.api.enums.bpm.BpmUnit;
import com.pluxbiosignals.beurer.api.enums.bpm.Hsd;
import com.pluxbiosignals.beurer.api.enums.bpm.PulseRange;

import junit.framework.TestCase;

import org.junit.Assert;

public class BpmMeasureStatusTest extends TestCase {

    public void testParseReply() throws Exception {
        byte[] received = {(byte) 0x56, (byte) 0x70, (byte) 0x00, (byte) 0x4D, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0xDF, (byte) 0x07,
                (byte) 0x01, (byte) 0x0E, (byte) 0x0A, (byte) 0x37, (byte) 0x00, (byte) 0x48,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40};

        BPMeasurement measurement = BPMeasurement.parseReply(received);


        BPMeasurement expected = new BPMeasurement(BpmUnit.MMHG, 112, 77,
                0, 1421232900000L, 72f, null,
                new BpmMeasureStatus(false, true, false,
                        PulseRange.IN_RANGE, true, Hsd.DETECTED));

        Assert.assertEquals(expected, measurement);

    }
}