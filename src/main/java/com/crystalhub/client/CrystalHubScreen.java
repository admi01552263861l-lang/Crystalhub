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
            // Auto Clicker toggle
            addRenderableWidget(Button.builder(
                Component.literal("Auto Clicker  •  " +
                    (CrystalHubConfig.autoClickerEnabled ? "ON" : "OFF")),
                b -> {
                    CrystalHubConfig.autoClickerEnabled =
                        !CrystalHubConfig.autoClickerEnabled;
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 128, 310, 24).build());

            // CPS -
            addRenderableWidget(Button.builder(
                Component.literal("CPS  -"),
                b -> {
                    CrystalHubConfig.setCps(CrystalHubConfig.cps - 1);
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 156, 90, 22).build());

            // CPS +
            addRenderableWidget(Button.builder(
                Component.literal("CPS  +"),
                b -> {
                    CrystalHubConfig.setCps(CrystalHubConfig.cps + 1);
                    rebuildWidgets();
                }
            ).bounds(x + 400, y + 156, 90, 22).build());

            // Aim Assist toggle
            addRenderableWidget(Button.builder(
                Component.literal("Aim Assist  •  " +
                    (CrystalHubConfig.aimAssistEnabled ? "ON" : "OFF")),
                b -> {
                    CrystalHubConfig.aimAssistEnabled =
                        !CrystalHubConfig.aimAssistEnabled;
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 188, 310, 24).build());

            // Target HEAD / BODY / LEGS
            addRenderableWidget(Button.builder(
                Component.literal("Target  " + CrystalHubConfig.aimAssistTarget),
                b -> {
                    String t = CrystalHubConfig.aimAssistTarget;
                    if ("HEAD".equals(t)) {
                        CrystalHubConfig.setAimAssistTarget("BODY");
                    } else if ("BODY".equals(t)) {
                        CrystalHubConfig.setAimAssistTarget("LEGS");
                    } else {
                        CrystalHubConfig.setAimAssistTarget("HEAD");
                    }
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 216, 150, 22).build());

            // Smooth -
            addRenderableWidget(Button.builder(
                Component.literal("Smooth -"),
                b -> {
                    CrystalHubConfig.setAimAssistSmooth(
                        CrystalHubConfig.aimAssistSmooth - 0.05f
                    );
                    rebuildWidgets();
                }
            ).bounds(x + 340, y + 216, 70, 22).build());

            // Smooth +
            addRenderableWidget(Button.builder(
                Component.literal("Smooth +"),
                b -> {
                    CrystalHubConfig.setAimAssistSmooth(
                        CrystalHubConfig.aimAssistSmooth + 0.05f
                    );
                    rebuildWidgets();
                }
            ).bounds(x + 420, y + 216, 70, 22).build());
        }

        if (selected.equals("Movement")) {
            addRenderableWidget(Button.builder(
                Component.literal("Speed  •  " +
                    (CrystalHubConfig.speedEnabled ? "ON" : "OFF")),
                b -> {
                    CrystalHubConfig.speedEnabled =
                        !CrystalHubConfig.speedEnabled;
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 140, 310, 28).build());
        }

        if (selected.equals("Visuals")) {
            addRenderableWidget(Button.builder(
                Component.literal("Player ESP  •  " +
                    (CrystalHubConfig.playerEspEnabled ? "ON" : "OFF")),
                b -> {
                    CrystalHubConfig.playerEspEnabled =
                        !CrystalHubConfig.playerEspEnabled;
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 140, 310, 28).build());
        }

        if (selected.equals("Settings")) {
            addRenderableWidget(Button.builder(
                Component.literal("Sounds  •  " +
                    (CrystalHubConfig.soundEffects ? "ON" : "OFF")),
                b -> {
                    CrystalHubConfig.soundEffects =
                        !CrystalHubConfig.soundEffects;
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 140, 310, 26).build());

            addRenderableWidget(Button.builder(
                Component.literal("Menu Animations  •  " +
                    (CrystalHubConfig.menuAnimations ? "ON" : "OFF")),
                b -> {
                    CrystalHubConfig.menuAnimations =
                        !CrystalHubConfig.menuAnimations;
                    rebuildWidgets();
                }
            ).bounds(x + 180, y + 172, 310, 26).build());
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

        g.fill(0, 0, width, height, 0xD9070312);
        g.fill(x - 3, y - 3, x + W + 3, y + H + 3, 0xFF7136A8);
        g.fill(x - 1, y - 1, x + W + 1, y + H + 1, 0xFF35204F);
        g.fill(x, y, x + W, y + H, 0xF20D0819);

        g.fill(x + 10, y + 10, x + W - 10, y + 65, 0xE51B102B);
        g.fill(x + 23, y + 20, x + 55, y + 52, 0xFF7135B4);
        g.fill(x + 29, y + 26, x + 49, y + 46, 0xFFC084FC);

        g.fill(x + 10, y + 74, x + 153, y + H - 12, 0xE8171025);
        g.fill(x + 163, y + 74, x + W - 10, y + H - 12, 0xB51B112B);

        int activeY = switch (selected) {
            case "Movement" -> 116;
            case "Visuals" -> 150;
            case "Player" -> 184;
            case "World" -> 218;
            case "Settings" -> 252;
            default -> 82;
        };

        g.fill(x + 15, y + activeY, x + 148, y + activeY + 27, 0xFF382052);
        g.fill(x + 15, y + activeY, x + 19, y + activeY + 27, 0xFFC084FC);

        g.fill(x + 176, y + 110, x + W - 25, y + 270, 0xC9221638);
        g.fill(x + 176, y + 110, x + W - 25, y + 112, 0xFF7136A8);

        super.extractRenderState(g, mouseX, mouseY, delta);

        g.text(font, "CRYSTAL", x + 65, y + 21, 0xFFFFFFFF, true);
        g.text(font, "H U B  CLIENT", x + 65, y + 39, 0xFFC084FC, false);

        g.text(font, "NAVIGATION", x + 25, y + 78, 0xFFAA8BC8, false);
        g.text(font, selected.toUpperCase(), x + 183, y + 91, 0xFFE9D5FF, true);
        g.fill(x + 180, y + 109, x + W - 25, y + 110, 0xFF583878);

        if (selected.equals("Combat")) {
            g.text(font, "Combat Modules", x + 190, y + 116, 0xFFFFFFFF, true);
            g.text(font,
                "CPS: " + CrystalHubConfig.cps +
                    "   |   Smooth: " +
                    String.format("%.2f", CrystalHubConfig.aimAssistSmooth),
                x + 190, y + 245, 0xFFC084FC, true);
            g.text(font, "Keys: G = Clicker  |  R = Aim Assist  |  Controls",
                x + 190, y + 258, 0xFFB9A8D0, false);
        } else if (selected.equals("Movement")) {
            g.text(font, "Movement Modules", x + 190, y + 120, 0xFFFFFFFF, true);
            g.text(font,
                "Multiplier: " +
                    String.format("%.2f", CrystalHubConfig.speedMultiplier),
                x + 190, y + 180, 0xFFC084FC, false);
        } else if (selected.equals("Visuals")) {
            g.text(font, "Visual Modules", x + 190, y + 120, 0xFFFFFFFF, true);
            g.text(font, "Player ESP boxes: " +
                    (CrystalHubConfig.playerEspBoxes ? "ON" : "OFF"),
                x + 190, y + 180, 0xFFC084FC, false);
        } else if (selected.equals("Player")) {
            g.text(font, "Player Settings", x + 190, y + 140, 0xFFFFFFFF, true);
        } else if (selected.equals("World")) {
            g.text(font, "World Settings", x + 190, y + 140, 0xFFFFFFFF, true);
        } else {
            g.text(font, "Client Settings", x + 190, y + 120, 0xFFFFFFFF, true);
            g.text(font, "Sounds, animations and keybinds.",
                x + 190, y + 210, 0xFFC084FC, false);
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
