
package com.crystalhub.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public final class CrystalHubClient implements ClientModInitializer {
    private static KeyMapping openMenuKey;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("crystal_hub", "main")
        );

        openMenuKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                "key.crystal_hub.open_menu",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_RIGHT_CONTROL,
                category
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (client.player != null) {
                    if (client.screen instanceof CrystalHubScreen) {
                        client.setScreen(null);
                    } else if (client.screen == null) {
                        client.setScreen(new CrystalHubScreen());
                    }
                }
            }
        });
    }
}
