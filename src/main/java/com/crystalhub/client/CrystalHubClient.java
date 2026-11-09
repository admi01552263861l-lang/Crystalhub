package com.crystalhub.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CrystalHubClient implements ClientModInitializer {

    private static final KeyMapping.Category CATEGORY =
        KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("crystal_hub", "main")
        );

    private static KeyMapping openMenuKey;
    private static KeyMapping autoClickerKey;
    private static KeyMapping aimAssistKey;

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

        aimAssistKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                "key.crystal_hub.toggle_aimassist",
                InputConstants.Type.KEYSYM,
                CrystalHubConfig.aimAssistKey,
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

            while (aimAssistKey.consumeClick()) {
                CrystalHubConfig.aimAssistEnabled =
                    !CrystalHubConfig.aimAssistEnabled;
            }

            updateAutoClicker(client);
            updateAimAssist(client);
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

        client.options.keyAttack.setDown(CrystalHubConfig.clickPressed);
    }

    private static void releaseAttack(Minecraft client) {
        CrystalHubConfig.clickPressed = false;
        client.options.keyAttack.setDown(false);
    }

    private static void updateAimAssist(Minecraft client) {
        if (!CrystalHubConfig.aimAssistEnabled) return;
        if (client.player == null || client.level == null) return;
        if (client.screen != null) return;

        LivingEntity target = findAimTarget(client);
        if (target == null) return;

        aimAt(client.player, target);
    }

    private static LivingEntity findAimTarget(Minecraft client) {
        Player self = client.player;
        LivingEntity best = null;
        double bestDist =
            CrystalHubConfig.aimAssistRange * CrystalHubConfig.aimAssistRange;
        float halfFov = CrystalHubConfig.aimAssistFov * 0.5f;
        AABB box = self.getBoundingBox().inflate(CrystalHubConfig.aimAssistRange);

        for (ArmorStand stand : client.level.getEntitiesOfClass(
                ArmorStand.class, box, e -> e.isAlive())) {
            if (!inFov(self, stand, halfFov)) continue;
            double d = self.distanceToSqr(stand);
            if (d < bestDist) {
                bestDist = d;
                best = stand;
            }
        }

        for (Player other : client.level.players()) {
            if (other == self || !other.isAlive() || other.isSpectator()) continue;
            if (!inFov(self, other, halfFov)) continue;
            double d = self.distanceToSqr(other);
            if (d < bestDist) {
                bestDist = d;
                best = other;
            }
        }
        return best;
    }

    private static boolean inFov(Player self, LivingEntity target, float halfFov) {
        Vec3 eye = self.getEyePosition();
        Vec3 aim = aimPoint(target);
        Vec3 to = aim.subtract(eye).normalize();
        double dot = self.getLookAngle().normalize().dot(to);
        return dot >= Math.cos(Math.toRadians(halfFov));
    }

    private static Vec3 aimPoint(LivingEntity target) {
        return target.position().add(
            0.0,
            target.getBbHeight() * CrystalHubConfig.aimHeightFactor(),
            0.0
        );
    }

    private static void aimAt(Player self, LivingEntity target) {
        Vec3 eye = self.getEyePosition();
        Vec3 aim = aimPoint(target);

        double dx = aim.x - eye.x;
        double dy = aim.y - eye.y;
        double dz = aim.z - eye.z;
        double horiz = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) (Math.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0f;
        float pitch = (float) (-(Math.atan2(dy, horiz) * (180.0 / Math.PI)));
        pitch = Mth.clamp(pitch, -90f, 90f);

        float t = Mth.clamp(CrystalHubConfig.aimAssistSmooth, 0.05f, 1.0f);
        float diff = Mth.wrapDegrees(yaw - self.getYRot());
        self.setYRot(self.getYRot() + diff * t);
        self.setXRot(Mth.lerp(t, self.getXRot(), pitch));
    }
}
