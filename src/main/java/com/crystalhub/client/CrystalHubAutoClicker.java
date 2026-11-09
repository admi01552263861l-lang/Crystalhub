
package com.crystalhub.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class CrystalHubAutoClicker {

    private CrystalHubAutoClicker() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!CrystalHubConfig.autoClickerEnabled) return;
            if (client.player == null || client.screen != null) return;

            long window = client.getWindow().handle();

            boolean activationPressed =
                CrystalHubConfig.autoClickKey != -1 &&
                GLFW.glfwGetKey(
                    window,
                    CrystalHubConfig.autoClickKey
                ) == GLFW.GLFW_PRESS;

            if (!activationPressed) return;

            long now = System.currentTimeMillis();

            if (now - CrystalHubConfig.lastClickTime <
                CrystalHubConfig.getClickInterval()) {
                return;
            }

            CrystalHubConfig.lastClickTime = now;

            Minecraft.getInstance().options.keyAttack
                .setDown(true);
        });
    }
}
