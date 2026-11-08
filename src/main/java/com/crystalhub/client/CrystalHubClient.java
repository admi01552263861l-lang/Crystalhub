
package com.crystalhub.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

public class CrystalHubClient implements ClientModInitializer {

    private static final KeyMapping.Category CATEGORY =
        KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("crystal_hub", "main")
        );

    private static KeyMapping openMenuKey;

    @Override
    public void onInitializeClient() {

        openMenuKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                "key.crystal_hub.open_menu",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_RIGHT_CONTROL,
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
        });
    }
}
