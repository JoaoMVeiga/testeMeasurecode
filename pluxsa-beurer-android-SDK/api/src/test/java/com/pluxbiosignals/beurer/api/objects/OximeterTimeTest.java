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

public class OximeterTimeTest extends TestCase {

    public void testParseReply() throws Exception {
        byte[] received ={(byte) 0xF9, (byte) 0x10, (byte) 0x04, (byte) 0x1A, (byte) 0x0A,
                (byte) 0x2A, (byte) 0x25, (byte) 0x63, (byte) 0x00, (byte) 0x63
        };

        OximeterTime expected = new OximeterTime(1461663757000L);

        OximeterTime time = OximeterTime.parseReply(received);

        Assert.assertEquals(expected, time);
    }
}