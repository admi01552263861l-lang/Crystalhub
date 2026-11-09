
package com.crystalhub.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CrystalHubScreen extends Screen {

    private static final int W = 540;
    private static final int H = 340;

    private String selected = "Combat";

    public CrystalHubScreen() {
        super(Component.literal("Crystal Hub"));
    }

    @Override
    protected void init() {
        int x = (width - W) / 2;
        int y = (height - H) / 2;

        addTab(x, y, "Combat", 14);
        addTab(x, y, "Movement", 105);
        addTab(x, y, "Visuals", 196);
        addTab(x, y, "Player", 287);
        addTab(x, y, "World", 378);
        addTab(x, y, "Settings", 469);

        if (selected.equals("Combat")) {
            addRenderableWidget(Button.builder(
                Component.literal("Auto Clicker  •  " +
                    (CrystalHubConfig.autoClickerEnabled ? "ON" : "OFF")),
                b -> {
                    CrystalHubConfig.autoClickerEnabled =
                        !CrystalHubConfig.autoClickerEnabled;
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 145, 310, 28).build());

            addRenderableWidget(Button.builder(
                Component.literal("Speed  -"),
                b -> {
                    CrystalHubConfig.cps =
                        Math.max(1, CrystalHubConfig.cps - 1);
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 185, 90, 25).build());

            addRenderableWidget(Button.builder(
                Component.literal("Speed  +"),
                b -> {
                    CrystalHubConfig.cps =
                        Math.min(10, CrystalHubConfig.cps + 1);
                    rebuildWidgets();
                }
            ).bounds(x + 400, y + 185, 90, 25).build());
        }

        addRenderableWidget(Button.builder(
            Component.literal("Close"),
            b -> onClose()
        ).bounds(x + W - 92, y + H - 38, 76, 24).build());
    }

    private void addTab(int x, int y, String name, int offset) {
        addRenderableWidget(Button.builder(
            Component.literal(name),
            b -> {
                selected = name;
                rebuildWidgets();
            }
        ).bounds(x + 16, y + 82 + (offset / 91) * 34, 125, 27).build());
    }

    @Override
    public void extractRenderState(
        GuiGraphicsExtractor g, int mouseX, int mouseY, float delta
    ) {
        int x = (width - W) / 2;
        int y = (height - H) / 2;

        // Ambient dark background
        g.fill(0, 0, width, height, 0xD9070312);

        // Outer purple glow layers
        g.fill(x - 3, y - 3, x + W + 3, y + H + 3, 0xFF7136A8);
        g.fill(x - 1, y - 1, x + W + 1, y + H + 1, 0xFF35204F);

        // Main glass panel
        g.fill(x, y, x + W, y + H, 0xF20D0819);

        // Header
        g.fill(x + 10, y + 10, x + W - 10, y + 65, 0xE51B102B);
        g.fill(x + 23, y + 20, x + 55, y + 52, 0xFF7135B4);
        g.fill(x + 29, y + 26, x + 49, y + 46, 0xFFC084FC);

        // Sidebar and content
        g.fill(x + 10, y + 74, x + 153, y + H - 12, 0xE8171025);
        g.fill(x + 163, y + 74, x + W - 10, y + H - 12, 0xB51B112B);

        // Selected tab highlight
        int activeY = switch (selected) {
            case "Movement" -> 116;
            case "Visuals" -> 150;
            case "Player" -> 184;
            case "World" -> 218;
            case "Settings" -> 252;
            default -> 82;
        };

        g.fill(x + 15, y + activeY, x + 148, y + activeY + 27,
            0xFF382052);
        g.fill(x + 15, y + activeY, x + 19, y + activeY + 27,
            0xFFC084FC);

        // Content card
        g.fill(x + 176, y + 121, x + W - 25, y + 224, 0xC9221638);
        g.fill(x + 176, y + 121, x + W - 25, y + 123, 0xFF7136A8);

        super.extractRenderState(g, mouseX, mouseY, delta);

        g.text(font, "CRYSTAL", x + 65, y + 21, 0xFFFFFFFF, true);
        g.text(font, "H U B  CLIENT", x + 65, y + 39, 0xFFC084FC, false);

        g.text(font, "NAVIGATION", x + 25, y + 78, 0xFFAA8BC8, false);
        g.text(font, selected.toUpperCase(), x + 183, y + 91,
            0xFFE9D5FF, true);

        g.fill(x + 180, y + 109, x + W - 25, y + 110, 0xFF583878);

        if (selected.equals("Combat")) {
            g.text(font, "Combat Modules", x + 190, y + 135,
                0xFFFFFFFF, true);

            g.text(font, "Auto Clicker", x + 190, y + 153,
                0xFFD8C5F2, false);

            g.text(font, "Current speed: " + CrystalHubConfig.cps + " CPS",
                x + 190, y + 216, 0xFFC084FC, true);

            g.text(font, "Activation key: G - Change in Controls",
                x + 190, y + 239, 0xFFB9A8D0, false);

            g.text(font, "Aim Assist settings will be added with its engine.",
                x + 190, y + 260, 0xFFAA8BC8, false);

        } else if (selected.equals("Movement")) {
            g.text(font, "Movement Modules", x + 190, y + 140,
                0xFFFFFFFF, true);
            g.text(font, "Speed module will be connected next.",
                x + 190, y + 165, 0xFFC084FC, false);

        } else if (selected.equals("Visuals")) {
            g.text(font, "Visual Modules", x + 190, y + 140,
                0xFFFFFFFF, true);
            g.text(font, "Player ESP will be connected to the renderer.",
                x + 190, y + 165, 0xFFC084FC, false);

        } else if (selected.equals("Player")) {
            g.text(font, "Player Settings", x + 190, y + 140,
                0xFFFFFFFF, true);

        } else if (selected.equals("World")) {
            g.text(font, "World Settings", x + 190, y + 140,
                0xFFFFFFFF, true);

        } else {
            g.text(font, "Client Settings", x + 190, y + 140,
                0xFFFFFFFF, true);
            g.text(font, "Sounds, animations and keybinds.",
                x + 190, y + 165, 0xFFC084FC, false);
        }

        g.text(font, "RIGHT CTRL  •  TOGGLE MENU",
            x + 180, y + 286, 0xFFAA8BC8, false);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
