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

public class ScaleBodyCompositionTest extends TestCase {

    public void testParseReply() {

        //Results shown in device screen:
        // Weight [kg] 61.8
        // BMI 22.7
        // Body fat [%] 25.5
        // Water [%] 51.2
        // Muscle mass [%] 36.3
        // Bone mass [kg] 2.9
        // BMR 13.33
        // AMR 19.33

        //NOTES:
        // soft lean mass = total mass - fat mass - bone mass
        // lean body mass = total mass - fat mass
        // bone = lean body mass - soft lean mass

        byte[] received = {
                (byte) 0x98, (byte) 0x03, //flag
                (byte) 0xFF, (byte) 0x00, //body fat
                (byte) 0xCD, (byte) 0x15, //BMR
                (byte) 0x6B, (byte) 0x01, //muscle %
                (byte) 0xAC, (byte) 0x21, //soft lean mass
                (byte) 0xB8, (byte) 0x18, //body water mass
                (byte) 0xAE, (byte) 0x0B //impedance
        };

        ScaleBodyComposition expected = new ScaleBodyComposition(ScaleUnit.SI, 25.5f,
                null, null, 1.333891f, 36.3f,
                null, null, 43.1f, 31.64f, 299f,
                null, null);

        ScaleBodyComposition measurement = ScaleBodyComposition.parseReply(received);

        Assert.assertEquals(expected, measurement);

    }
}