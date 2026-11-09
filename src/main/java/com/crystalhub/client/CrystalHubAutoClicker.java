
package com.crystalhub.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;

public final class CrystalHubAutoClicker {

    private static KeyMapping toggleKey;

    private CrystalHubAutoClicker() {}

    public static void register() {
        KeyMapping.Category category =
            KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath("crystal_hub", "combat")
            );

        toggleKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                "key.crystal_hub.toggle_autoclicker",
                InputConstants.Type.KEYSYM,
                71,
                category
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                CrystalHubConfig.autoClickerEnabled =
                    !CrystalHubConfig.autoClickerEnabled;

                CrystalHubConfig.lastClickTime = 0L;
            }

            if (client.player == null || client.screen != null) {
                releaseAttack(client);
                return;
            }

            if (!CrystalHubConfig.autoClickerEnabled) {
                releaseAttack(client);
                return;
            }

            long now = System.currentTimeMillis();
            long interval = CrystalHubConfig.getClickInterval();

            if (now - CrystalHubConfig.lastClickTime >= interval) {
                CrystalHubConfig.lastClickTime = now;
                CrystalHubConfig.clickPressed = true;
            } else {
                CrystalHubConfig.clickPressed = false;
            }

            client.options.keyAttack.setDown(
                CrystalHubConfig.clickPressed
            );
        });
    }

    private static void releaseAttack(Minecraft client) {
        CrystalHubConfig.clickPressed = false;
        client.options.keyAttack.setDown(false);
    }
}
