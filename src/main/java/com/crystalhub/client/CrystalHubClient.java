
package com.crystalhub.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class CrystalHubClient implements ClientModInitializer {

    private static final KeyMapping.Category CATEGORY =
        KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("crystal_hub", "main")
        );

    private static KeyMapping openMenuKey;
    private static KeyMapping autoClickerKey;

    @Override
    public void onInitializeClient() {

        openMenuKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                "key.crystal_hub.open_menu",
                InputConstants.Type.KEYSYM,
                345,
                CATEGORY
            )
        );

        autoClickerKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                "key.crystal_hub.toggle_autoclicker",
                InputConstants.Type.KEYSYM,
                CrystalHubConfig.autoClickerKey,
                CATEGORY
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (openMenuKey.consumeClick()) {
                if (client.screen instanceof CrystalHubScreen) {
                    client.setScreen(null);
                } else if (client.screen == null) {
                    client.setScreen(new CrystalHubScreen());
                }
            }

            while (autoClickerKey.consumeClick()) {
                CrystalHubConfig.autoClickerEnabled =
                    !CrystalHubConfig.autoClickerEnabled;

                CrystalHubConfig.lastClickTime = 0L;
            }

            updateAutoClicker(client);
        });
    }

    private static void updateAutoClicker(Minecraft client) {

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
    }

    private static void releaseAttack(Minecraft client) {
        CrystalHubConfig.clickPressed = false;
        client.options.keyAttack.setDown(false);
    }
}
