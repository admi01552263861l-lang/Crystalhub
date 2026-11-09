package com.crystalhub.client;

public final class CrystalHubConfig {

    private CrystalHubConfig() {}

    // Combat
    public static boolean autoClickerEnabled = false;
    public static int cps = 5;

    // Aim Assist
    public static boolean aimAssistEnabled = false;
    public static String aimAssistTarget = "BODY"; // HEAD, BODY, LEGS
    public static float aimAssistSmooth = 0.35f;
    public static float aimAssistFov = 90f;
    public static double aimAssistRange = 64.0;

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

    // Keys (GLFW)
    public static int autoClickerKey = 71; // G
    public static int aimAssistKey = 82;   // R
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

    public static void setAimAssistSmooth(float value) {
        aimAssistSmooth = Math.max(0.05f, Math.min(value, 1.0f));
    }

    public static void setAimAssistFov(float value) {
        aimAssistFov = Math.max(20f, Math.min(value, 180f));
    }

    public static void setAimAssistRange(double value) {
        aimAssistRange = Math.max(4.0, Math.min(value, 128.0));
    }

    public static double aimHeightFactor() {
        return switch (aimAssistTarget) {
            case "HEAD" -> 0.9;
            case "LEGS" -> 0.15;
            default -> 0.5;
        };
    }
}
