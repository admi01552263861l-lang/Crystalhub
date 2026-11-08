
package com.crystalhub.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CrystalHubScreen extends Screen {
    private static final int W = 500;
    private static final int H = 310;
    private String selected = "Home";

    public CrystalHubScreen() {
        super(Component.literal("Crystal Hub"));
    }

    @Override
    protected void init() {
        int x = (width - W) / 2;
        int y = (height - H) / 2;

        tab(x, y, "Home", 84);
        tab(x, y, "Modules", 116);
        tab(x, y, "Settings", 148);
        tab(x, y, "About", 180);

        addRenderableWidget(Button.builder(
            Component.literal("Close"), b -> onClose()
        ).bounds(x + W - 92, y + H - 38, 76, 24).build());
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
        GuiGraphicsExtractor g, int mouseX, int mouseY, float delta
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
            case "Modules" -> 116;
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
        g.text(font, "YOUR CLIENT  /  YOUR SPACE", x + 64, y + 39, 0xFFA9BFFF, false);
        g.text(font, "NAVIGATION", x + 25, y + 78, 0xFF829BCB, false);

        g.text(font, selected.toUpperCase(), x + 178, y + 91, 0xFFC5D5FF, true);
        g.fill(x + 178, y + 111, x + W - 32, y + 112, 0xFF34486D);

        String heading = switch (selected) {
            case "Modules" -> "Your modules";
            case "Settings" -> "Personalize your hub";
            case "About" -> "Made for your Minecraft client";
            default -> "Welcome to Crystal Hub";
        };

        String description = switch (selected) {
            case "Modules" -> "Your enabled features will appear here.";
            case "Settings" -> "Customize your interface here.";
            case "About" -> "A crystal-inspired Minecraft interface.";
            default -> "A clean space for your favorite tools.";
        };

        g.text(font, heading, x + 178, y + 132, 0xFFFFFFFF, true);
        g.text(font, description, x + 178, y + 151, 0xFFB4C2DD, false);

        g.fill(x + 178, y + 184, x + 316, y + 246, 0xFF17243B);
        g.fill(x + 326, y + 184, x + W - 28, y + 246, 0xFF17243B);

        g.fill(x + 178, y + 184, x + 316, y + 186, 0xFF647FEA);
        g.fill(x + 326, y + 184, x + W - 28, y + 186, 0xFF9A79E8);

        g.text(font, "CRYSTAL CORE", x + 190, y + 198, 0xFF9EB9FF, true);
        g.text(font, "READY", x + 190, y + 220, 0xFFFFFFFF, false);
        g.text(font, "INTERFACE", x + 338, y + 198, 0xFFC2A7FF, true);
        g.text(font, "ACTIVE", x + 338, y + 220, 0xFFFFFFFF, false);

        g.text(font, "RIGHT CTRL  -  TOGGLE MENU",
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
