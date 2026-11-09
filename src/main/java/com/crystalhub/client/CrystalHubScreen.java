
package com.crystalhub.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CrystalHubScreen extends Screen {

    private static final int W = 500;
    private static final int H = 310;

    private String selected = "Combat";

    public CrystalHubScreen() {
        super(Component.literal("Crystal Hub"));
    }

    @Override
    protected void init() {
        int x = (width - W) / 2;
        int y = (height - H) / 2;

        tab(x, y, "Combat", 84);
        tab(x, y, "Settings", 116);
        tab(x, y, "About", 148);

        if (selected.equals("Combat")) {
            addRenderableWidget(Button.builder(
                Component.literal("Auto Clicker: " +
                    (CrystalHubConfig.autoClickerEnabled ? "ON" : "OFF")),
                b -> {
                    CrystalHubConfig.autoClickerEnabled =
                        !CrystalHubConfig.autoClickerEnabled;
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 135, 250, 25).build());

            addRenderableWidget(Button.builder(
                Component.literal("Speed: " + CrystalHubConfig.cps + " / 10"),
                b -> {
                    CrystalHubConfig.cps =
                        CrystalHubConfig.cps >= 10
                            ? 1 : CrystalHubConfig.cps + 1;
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 170, 250, 25).build());
        }

        addRenderableWidget(Button.builder(
            Component.literal("Close"),
            b -> onClose()
        ).bounds(x + W - 92, y + H - 38, 76, 24).build());
    }

    private void tab(int x, int y, String name, int offset) {
        addRenderableWidget(Button.builder(
            Component.literal(name),
            b -> {
                selected = name;
                rebuildWidgets();
            }
        ).bounds(x + 18, y + offset, 116, 25).build());
    }

    @Override
    public void extractRenderState(
        GuiGraphicsExtractor g, int mouseX, int mouseY, float delta
    ) {
        int x = (width - W) / 2;
        int y = (height - H) / 2;

        g.fill(0, 0, width, height, 0xA6070B18);
        g.fill(x - 2, y - 2, x + W + 2, y + H + 2, 0xFF6478E8);
        g.fill(x, y, x + W, y + H, 0xF10C1325);

        g.fill(x + 10, y + 10, x + W - 10, y + 62, 0xFF172542);
        g.fill(x + 22, y + 20, x + 54, y + 52, 0xFF344D91);
        g.fill(x + 27, y + 25, x + 49, y + 47, 0xFF91B8FF);

        g.fill(x + 10, y + 72, x + 148, y + H - 12, 0xE9111D35);

        int activeY = switch (selected) {
            case "Settings" -> 116;
            case "About" -> 148;
            default -> 84;
        };

        g.fill(x + 15, y + activeY, x + 139, y + activeY + 25, 0xFF293E70);
        g.fill(x + 15, y + activeY, x + 18, y + activeY + 25, 0xFF8EB5FF);
        g.fill(x + 156, y + 72, x + W - 10, y + H - 12, 0xB719263F);

        super.extractRenderState(g, mouseX, mouseY, delta);

        g.text(font, "CRYSTAL HUB", x + 64, y + 22, 0xFFFFFFFF, true);
        g.text(font, "YOUR CLIENT / YOUR SPACE", x + 64, y + 39, 0xFFA9BFFF, false);
        g.text(font, "NAVIGATION", x + 25, y + 78, 0xFF829BCB, false);
        g.text(font, selected.toUpperCase(), x + 178, y + 91, 0xFFC5D5FF, true);

        g.fill(x + 178, y + 111, x + W - 32, y + 112, 0xFF34486D);

        if (selected.equals("Combat")) {
            g.text(font, "Combat modules", x + 178, y + 132, 0xFFFFFFFF, true);
            g.text(font, "Activation key: G (change in Controls)",
                x + 180, y + 215, 0xFFB4C2DD, false);
            g.text(font, "Speed range: 1 - 10 CPS",
                x + 180, y + 230, 0xFFB4C2DD, false);
        } else if (selected.equals("Settings")) {
            g.text(font, "Personalize your hub",
                x + 178, y + 132, 0xFFFFFFFF, true);
        } else {
            g.text(font, "Made for your Minecraft client",
                x + 178, y + 132, 0xFFFFFFFF, true);
        }

        g.text(font, "RIGHT CTRL - TOGGLE MENU",
            x + 178, y + 270, 0xFF8297C7, false);
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
