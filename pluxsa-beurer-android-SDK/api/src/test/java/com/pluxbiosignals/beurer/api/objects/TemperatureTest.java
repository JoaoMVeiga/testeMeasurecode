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

import com.pluxbiosignals.beurer.api.enums.thermometer.TemperatureType;
import com.pluxbiosignals.beurer.api.enums.thermometer.TemperatureUnit;

import junit.framework.TestCase;

import org.junit.Assert;

public class TemperatureTest extends TestCase {

    public void testParseReply() throws Exception {
        byte[] received = {(byte) 0x06, (byte) 0x76, (byte) 0x01, (byte) 0x00, (byte) 0xFF, (byte) 0xE3, (byte) 0x07, (byte) 0x03, (byte) 0x0E, (byte) 0x17, (byte) 0x0D, (byte) 0x02, (byte) 0x02
        };

        Temperature expected = new Temperature(TemperatureUnit.CELSIUS, 1552605182000L,
                TemperatureType.BODY, false, 37.4f);

        Temperature temp = Temperature.parseReply(received);

        Assert.assertEquals(expected, temp);
    }
}