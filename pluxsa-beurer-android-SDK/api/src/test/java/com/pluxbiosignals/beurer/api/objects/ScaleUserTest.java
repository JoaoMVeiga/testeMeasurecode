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

import com.pluxbiosignals.beurer.api.enums.scale.Gender;

import junit.framework.TestCase;

import org.junit.Assert;

public class ScaleUserTest extends TestCase {

    public void testParseReply() {
        byte[] received = new byte[]{
                (byte) 0x00, (byte) 0x01, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xC7, (byte) 0x07, (byte) 0x0A,
                (byte) 0x01, (byte) 0xA5, (byte) 0x01, (byte) 0x03};

        ScaleUser actual = ScaleUser.parseReply(received);

        ScaleUser expected = new ScaleUser( 1,686275200000L, 165,
                Gender.FEMALE, 3);

        Assert.assertEquals(expected, actual);
    }
}