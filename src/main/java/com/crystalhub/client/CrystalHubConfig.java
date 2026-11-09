
package com.crystalhub.client;

public final class CrystalHubConfig {

    private CrystalHubConfig() {}

    public static boolean autoClickerEnabled = false;

    // Speed: 1 to 10
    public static int cps = 5;

    public static long lastClickTime = 0L;

    public static boolean clickPressed = false;

    public static long getClickInterval() {
        int speed = Math.max(1, Math.min(cps, 10));
        return 1000L / speed;
    }
}
