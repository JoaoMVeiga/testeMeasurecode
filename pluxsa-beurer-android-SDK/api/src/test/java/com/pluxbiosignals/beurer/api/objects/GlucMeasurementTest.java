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

import com.pluxbiosignals.beurer.api.enums.glucometer.GlucLocation;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucType;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucUnit;

import junit.framework.TestCase;

import org.junit.Assert;

public class GlucMeasurementTest extends TestCase {

    public void testParseReply() throws Exception {
        byte[] received = new byte[]{
                (byte) 0x12, (byte) 0x06, (byte) 0x00, (byte) 0xE5,
                (byte) 0x07, (byte) 0x05, (byte) 0x0B, (byte) 0x0E,
                (byte) 0x27, (byte) 0x00, (byte) 0x6C, (byte) 0xB0,
                (byte) 0x11};

        GlucMeasurement actual = GlucMeasurement.parseReply(received);

        GlucMeasurement expected = new GlucMeasurement(GlucUnit.KG_L, 6,
                1620740340000L, 0.00108f, GlucType.CAPILLARY_WHOLE_BLOOD,
                GlucLocation.FINGER, null);

        Assert.assertEquals(expected, actual);
    }
}