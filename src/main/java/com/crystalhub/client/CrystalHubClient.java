
package com.crystalhub.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class CrystalHubClient implements ClientModInitializer {

    private static KeyMapping openMenuKey;

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                "key.crystal_hub.open_menu",
                GLFW.GLFW_KEY_RIGHT_CONTROL,
                KeyMapping.Category.MISC
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                Screen current = client.screen;

                if (current instanceof CrystalHubScreen) {
                    client.setScreen(null);
                } else if (current == null) {
                    client.setScreen(new CrystalHubScreen());
                }
            }
        });
    }
}
