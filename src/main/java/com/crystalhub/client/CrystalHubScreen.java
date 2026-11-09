
package com.crystalhub.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CrystalHubScreen extends Screen {

    private static final int W = 500;
    private static final int H = 310;

    private String selected = "Combat";

    private boolean autoClicker = false;
    private boolean playerEsp = false;
    private boolean chestEsp = false;

    private int cps = 10;

    public CrystalHubScreen() {
        super(Component.literal("Crystal Hub"));
    }

    @Override
    protected void init() {
        int x = (width - W) / 2;
        int y = (height - H) / 2;

        tab(x, y, "Combat", 84);
        tab(x, y, "ESP", 116);
        tab(x, y, "Settings", 148);
        tab(x, y, "About", 180);

        if (selected.equals("Combat")) {
            addRenderableWidget(Button.builder(
                toggleText("Auto Clicker", autoClicker),
                b -> {
                    autoClicker = !autoClicker;
                    rebuildWidgets();
                }
            ).bounds(x + 178, y + 166, 130, 25).build());

            addRenderableWidget(Button.builder(
                Component.literal("CPS: " + cps),
                b -> {
                    cps += 5;
                    if (cps > 20) cps = 5;
                    rebuildWidgets();
                }
            ).bounds(x + 326, y + 166, 120, 25).build());
        }

        if (selected.equals("ESP")) {
            addRenderableWidget(Button.builder(
                toggleText("Player ESP", playerEsp),
                b -> {
                    playerEsp = !playerEsp;
                    rebuildWidgets();
                }
            ).bounds(x + 178, y + 150, 268, 25).build());

            addRenderableWidget(Button.builder(
                toggleText("Chest ESP", chestEsp),
                b -> {
                    chestEsp = !chestEsp;
                    rebuildWidgets();
                }
            ).bounds(x + 178, y + 190, 268, 25).build());
        }

        addRenderableWidget(Button.builder(
            Component.literal("Close"), b -> onClose()
        ).bounds(x + W - 92, y + H - 38, 76, 24).build());
    }

    private Component toggleText(String name, boolean enabled) {
        return Component.literal(
            name + ": " + (enabled ? "ON" : "OFF")
        );
    }

    private void tab(int x, int y, String name, int offset) {
        addRenderableWidget(Button.builder(
            Component.literal(name), b -> {
                selected = name;
                rebuildWidgets();
            }
        ).bounds(x + 18, y + offset, 116, 25).build());
    }

    @Override
    public void extractRenderState(
        GuiGraphicsExtractor g,
        int mouseX,
        int mouseY,
        float delta
    ) {
        int x = (width - W) / 2;
        int y = (height - H) / 2;

        g.fill(0, 0, width, height, 0xA6070B18);
        g.fill(x - 2, y - 2, x + W + 2, y + H + 2, 0xFF6478E8);
        g.fill(x, y, x + W, y + H, 0xF10C1325);

        // Header
        g.fill(x + 10, y + 10, x + W - 10, y + 62, 0xFF172542);
        g.fill(x + 22, y + 20, x + 54, y + 52, 0xFF344D91);
        g.fill(x + 27, y + 25, x + 49, y + 47, 0xFF91B8FF);

        // Sidebar
        g.fill(x + 10, y + 72, x + 148, y + H - 12, 0xE9111D35);

        int activeY = switch (selected) {
            case "ESP" -> 116;
            case "Settings" -> 148;
            case "About" -> 180;
            default -> 84;
        };

        g.fill(x + 15, y + activeY, x + 139, y + activeY + 25, 0xFF293E70);
        g.fill(x + 15, y + activeY, x + 18, y + activeY + 25, 0xFF8EB5FF);

        // Main panel
        g.fill(x + 156, y + 72, x + W - 10, y + H - 12, 0xB719263F);

        super.extractRenderState(g, mouseX, mouseY, delta);

        g.text(font, "CRYSTAL HUB", x + 64, y + 22, 0xFFFFFFFF, true);
        g.text(font, "YOUR CLIENT / YOUR SPACE", x + 64, y + 39, 0xFFA9BFFF, false);
        g.text(font, "NAVIGATION", x + 25, y + 78, 0xFF829BCB, false);

        g.text(font, selected.toUpperCase(), x + 178, y + 91, 0xFFC5D5FF, true);
        g.fill(x + 178, y + 111, x + W - 32, y + 112, 0xFF34486D);

        switch (selected) {
            case "Combat" -> renderCombat(g, x, y);
            case "ESP" -> renderESP(g, x, y);
            case "Settings" -> {
                g.text(font, "Personalize your hub", x + 178, y + 140, 0xFFFFFFFF, true);
                g.text(font, "More settings coming soon.", x + 178, y + 160, 0xFFB4C2DD, false);
            }
            case "About" -> {
                g.text(font, "Crystal Hub", x + 178, y + 140, 0xFFFFFFFF, true);
                g.text(font, "A crystal-inspired Minecraft client.", x + 178, y + 160, 0xFFB4C2DD, false);
            }
        }

        g.text(font, "RIGHT CTRL - TOGGLE MENU",
            x + 178, y + 270, 0xFF8297C7, false);
    }

    private void renderCombat(GuiGraphicsExtractor g, int x, int y) {
        g.text(font, "Combat controls", x + 178, y + 132, 0xFFFFFFFF, true);
        g.text(font, "Auto Clicker", x + 178, y + 151, 0xFF9EB9FF, false);
        g.text(font, "Click rate: " + cps + " CPS", x + 326, y + 151, 0xFFC2A7FF, false);

        g.text(font, "Toggle the setting and adjust CPS.",
            x + 178, y + 207, 0xFFB4C2DD, false);
        g.text(font, "Status: " + (autoClicker ? "Enabled" : "Disabled"),
            x + 178, y + 228, 0xFF9EB9FF, false);
    }

    private void renderESP(GuiGraphicsExtractor g, int x, int y) {
        g.text(font, "ESP options", x + 178, y + 132, 0xFFFFFFFF, true);

        g.text(font, "Player ESP", x + 178, y + 222, 0xFFFF7777, true);
        g.text(font, "Chest ESP", x + 326, y + 222, 0xFFFF7777, true);

        g.text(font, "Player: " + (playerEsp ? "ON" : "OFF"),
            x + 178, y + 245, 0xFFB4C2DD, false);
        g.text(font, "Chest: " + (chestEsp ? "ON" : "OFF"),
            x + 326, y + 245, 0xFFB4C2DD, false);
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
