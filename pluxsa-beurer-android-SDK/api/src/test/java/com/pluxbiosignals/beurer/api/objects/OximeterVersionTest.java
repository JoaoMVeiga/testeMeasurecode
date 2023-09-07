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

public class OximeterVersionTest extends TestCase {

    public void testParseReply() throws Exception {
        byte[] reply = {(byte) 0xF2, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x01,
                (byte) 0x07, (byte) 0x00, (byte) 0x01};


        OximeterVersion expected = new OximeterVersion("0.7.0", "0.7.1");
        OximeterVersion version = OximeterVersion.parseReply(reply);

        Assert.assertEquals(expected, version);
    }
}