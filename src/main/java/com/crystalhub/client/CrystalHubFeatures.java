package com.crystalhub.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * كل محركات Combat/Movement هنا — ملف واحد.
 */
public final class CrystalHubFeatures {
    private CrystalHubFeatures() {}

    public static void tick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        tickAutoClicker(client);
        tickAimAssist(client);
        // tickSpeed(client);
        // tickEsp يُرسم عادة في WorldRenderEvents وليس هنا
    }

    // ========== Auto Clicker ==========
    private static long lastClickMs = 0;

    private static void tickAutoClicker(MinecraftClient client) {
        if (!CrystalHubConfig.autoClickerEnabled) return;
        if (client.currentScreen != null) return;

        int cps = MathHelper.clamp(CrystalHubConfig.autoClickerCps, 1, 10);
        long interval = 1000L / cps;
        long now = System.currentTimeMillis();
        if (now - lastClickMs < interval) return;
        lastClickMs = now;

        // نقر يسار — عدّل حسب API 26.1.2 عندك إن تغيّر الاسم
        pcallDoAttack(client);
    }

    private static void pcallDoAttack(MinecraftClient client) {
        try {
            // Yarn حديث غالباً:
            client.doAttack();
        } catch (Throwable t) {
            // بديل قديم أحياناً عبر interactionManager
            try {
                if (client.crosshairTarget != null
                        && client.interactionManager != null
                        && client.player != null) {
                    // حسب نوع الهدف (كيان / بلوك) — اربطه بمنطقك السابق من ملف الأوتو كليكر
                }
            } catch (Throwable ignored) {}
        }
    }

    // ========== Aim Assist ==========
    private static void tickAimAssist(MinecraftClient client) {
        if (!CrystalHubConfig.aimAssistEnabled) return;
        if (client.currentScreen != null) return;

        LivingEntity target = findAimTarget(client);
        if (target == null) return;

        PlayerEntity self = client.player;
        Vec3d eye = self.getEyePos();
        double factor = CrystalHubConfig.aimAssistPoint.heightFactor;
        Vec3d aim = target.getPos().add(0.0, target.getHeight() * factor, 0.0);

        double dx = aim.x - eye.x;
        double dy = aim.y - eye.y;
        double dz = aim.z - eye.z;
        double horiz = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) (MathHelper.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0f;
        float pitch = (float) (-(MathHelper.atan2(dy, horiz) * (180.0 / Math.PI)));
        pitch = MathHelper.clamp(pitch, -90f, 90f);

        float t = MathHelper.clamp(CrystalHubConfig.aimAssistSmooth, 0.05f, 1.0f);
        float diff = MathHelper.wrapDegrees(yaw - self.getYaw());
        self.setYaw(self.getYaw() + diff * t);
        self.setPitch(MathHelper.lerp(t, self.getPitch(), pitch));
    }

    private static LivingEntity findAimTarget(MinecraftClient client) {
        PlayerEntity self = client.player;
        LivingEntity best = null;
        double bestDist = CrystalHubConfig.aimAssistRange * CrystalHubConfig.aimAssistRange;
        float halfFov = CrystalHubConfig.aimAssistFov * 0.5f;

        for (ArmorStandEntity stand : client.world.getEntitiesByClass(
                ArmorStandEntity.class,
                self.getBoundingBox().expand(CrystalHubConfig.aimAssistRange),
                e -> e.isAlive())) {
            if (!inFov(self, stand, halfFov)) continue;
            double d = self.squaredDistanceTo(stand);
            if (d < bestDist) { bestDist = d; best = stand; }
        }

        for (PlayerEntity other : client.world.getPlayers()) {
            if (other == self || !other.isAlive() || other.isSpectator()) continue;
            if (!inFov(self, other, halfFov)) continue;
            double d = self.squaredDistanceTo(other);
            if (d < bestDist) { bestDist = d; best = other; }
        }
        return best;
    }

    private static boolean inFov(PlayerEntity self, LivingEntity target, float halfFov) {
        Vec3d eye = self.getEyePos();
        double f = CrystalHubConfig.aimAssistPoint.heightFactor;
        Vec3d to = target.getPos().add(0, target.getHeight() * f, 0).subtract(eye).normalize();
        double dot = self.getRotationVec(1.0f).normalize().dotProduct(to);
        return dot >= Math.cos(Math.toRadians(halfFov));
    }
}
