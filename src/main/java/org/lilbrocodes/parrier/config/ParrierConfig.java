package org.lilbrocodes.parrier.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class ParrierConfig extends MidnightConfig {
    public static final String PARRY_OPTIONS = "parry_options";
    public static final String FREEZE_OPTIONS = "freeze_options";

    @Entry(category = PARRY_OPTIONS) public static int parryRange = 3;
    @Comment(category = PARRY_OPTIONS, centered = true) public static String parryStrengthComment;
    @Entry(category = PARRY_OPTIONS, isSlider = true, min = -10, max = 10) public static float parryStrength = 4;
    @Comment(category = PARRY_OPTIONS, centered = true) public static String reParryComment;
    @Entry(category = PARRY_OPTIONS) public static boolean reParry = false;
    @Comment(category = PARRY_OPTIONS, centered = true) public static String backParryComment;
    @Entry(category = PARRY_OPTIONS) public static boolean backParry = true;

    @Entry(category = FREEZE_OPTIONS) public static int gameFreezeTicks = 20;
    @Comment(category = FREEZE_OPTIONS, centered = true) public static String gameFreezeRangeComment;
    @Entry(category = FREEZE_OPTIONS) public static int gameFreezeRange = 100;
    @Entry(category = FREEZE_OPTIONS) public static boolean flashScreen = true;
    @Entry(category = FREEZE_OPTIONS) public static boolean freezeMovement = true;
    @Entry(category = FREEZE_OPTIONS) public static boolean freezeAnimations = true;
    @Entry(category = FREEZE_OPTIONS) public static boolean freezeAi = true;
    @Entry(category = FREEZE_OPTIONS) public static boolean freezeParticles = true;
    @Entry(category = FREEZE_OPTIONS) public static boolean freezeBlocks = false;
}
