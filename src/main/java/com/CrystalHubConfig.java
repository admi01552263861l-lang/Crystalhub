
package com.crystalhub.client;

public final class CrystalHubConfig {

    private CrystalHubConfig() {}

    public static boolean autoClickerEnabled = false;
    public static boolean playerEspEnabled = false;
    public static boolean chestEspEnabled = false;

    public static int cps = 10;

    public static int autoClickKey = -1;

    public static long lastClickTime = 0L;

    public static long getClickInterval() {
        int safeCps = Math.max(1, Math.min(cps, 20));
        return 1000L / safeCps;
    }
}
