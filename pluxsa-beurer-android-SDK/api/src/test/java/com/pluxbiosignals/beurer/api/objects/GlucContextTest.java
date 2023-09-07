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

import com.pluxbiosignals.beurer.api.enums.glucometer.GlucMeal;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucTester;
import com.pluxbiosignals.beurer.api.enums.glucometer.GlucTesterHealth;

import junit.framework.TestCase;

import org.junit.Assert;

public class GlucContextTest extends TestCase {

    public void testParseReply() throws Exception {
        byte[] received = new byte[]{
                (byte) 0x02, (byte) 0x06, (byte) 0x00, (byte) 0x01};


        GlucContext expected = new GlucContext(6, null, GlucMeal.PREPRANDIAL,
                GlucTester.TESTER_NOT_AVAILABLE, GlucTesterHealth.HEALTH_NO, null,
                null, null);

        GlucContext context = GlucContext.parseReply(received);

        Assert.assertEquals(expected, context);
    }
}