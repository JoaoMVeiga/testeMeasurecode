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

public class OximeterStorageTest extends TestCase {

    public void testParseReply() throws Exception {

        byte[] received = {(byte) 0xE0, (byte) 0x05, (byte) 0x08, (byte) 0x00, (byte) 0x08,
                (byte) 0x00, (byte) 0x75};

        OximeterStorage expected = new OximeterStorage(8, 8);

        OximeterStorage storage = OximeterStorage.parseReply(received);

        Assert.assertEquals(expected, storage);
    }
}