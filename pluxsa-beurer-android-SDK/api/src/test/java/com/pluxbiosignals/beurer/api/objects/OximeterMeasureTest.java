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

import junit.framework.TestCase;

import org.junit.Assert;

public class OximeterMeasureTest extends TestCase {

    public void testParseReply() throws Exception {
        byte[] received = {
                (byte) 0xE9, (byte) 0x00, (byte) 0x0F, (byte) 0x0B, (byte) 0x06, (byte) 0x0D,
                (byte) 0x14, (byte) 0x25, (byte) 0x0F, (byte) 0x0B, (byte) 0x06, (byte) 0x0D,
                (byte) 0x14, (byte) 0x3A, (byte) 0x00, (byte) 0x00, (byte) 0x16, (byte) 0x60,
                (byte) 0x60, (byte) 0x60, (byte) 0x6F, (byte) 0x6E, (byte) 0x6E, (byte) 0x4B};

        OximeterMeasure actual = OximeterMeasure.parseReply(received);

        OximeterMeasure expected = new OximeterMeasure(1446816037000L,1446816058000L,
                22,96, 96,96,111,110,110);

        Assert.assertEquals(expected, actual);
    }
}