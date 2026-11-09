
package com.crystalhub.client;

public final class CrystalHubConfig {

    private CrystalHubConfig() {}

    // Combat
    public static boolean autoClickerEnabled = false;
    public static int cps = 5;

    // Aim Assist settings
    public static boolean aimAssistEnabled = false;

    // Options: HEAD, BODY, LEGS
    public static String aimAssistTarget = "BODY";

    // Movement
    public static boolean speedEnabled = false;
    public static double speedMultiplier = 1.2;

    // Visuals
    public static boolean playerEspEnabled = false;
    public static boolean playerEspBoxes = true;

    // Interface
    public static boolean soundEffects = true;
    public static boolean menuAnimations = true;
    public static boolean moduleAnimations = true;

    // Key defaults
    public static int autoClickerKey = 71;
    public static int aimAssistKey = 0;
    public static int speedKey = 0;
    public static int playerEspKey = 0;

    // Auto Clicker runtime
    public static long lastClickTime = 0L;
    public static boolean clickPressed = false;

    public static long getClickInterval() {
        int speed = Math.max(1, Math.min(cps, 10));
        return 1000L / speed;
    }

    public static void setCps(int value) {
        cps = Math.max(1, Math.min(value, 10));
    }

    public static void setSpeedMultiplier(double value) {
        speedMultiplier = Math.max(1.0, Math.min(value, 2.0));
    }

    public static void setAimAssistTarget(String target) {
        if (target == null) {
            aimAssistTarget = "BODY";
            return;
        }

        switch (target.toUpperCase()) {
            case "HEAD" -> aimAssistTarget = "HEAD";
            case "LEGS" -> aimAssistTarget = "LEGS";
            default -> aimAssistTarget = "BODY";
        }
    }
}
