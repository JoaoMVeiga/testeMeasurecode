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

import com.pluxbiosignals.beurer.api.enums.scale.ScaleUnit;

import junit.framework.TestCase;

import org.junit.Assert;

public class ScaleMeasurementTest extends TestCase {

    public void testParseReply() {

        byte[] received = {
                0x0E, 0x4C, 0x30, (byte) 0xE7, 0x07, 0x08, 0x11, 0x0F,
                0x14, 0x17, 0x01, (byte) 0xE3, 0x00, 0x72, 0x06
        };

        ScaleMeasurement expected = new ScaleMeasurement(ScaleUnit.SI, 61.82f,
                1692282023000L, null, 22.7f, 1.65f);

        ScaleMeasurement measurement = ScaleMeasurement.parseReply(received);

        Assert.assertEquals(expected, measurement);
    }
}