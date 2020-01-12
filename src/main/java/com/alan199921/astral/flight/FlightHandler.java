package com.alan199921.astral.flight;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class FlightHandler {
    public static void handleAstralFlight(PlayerEntity player) {
        if (InputHandler.isHoldingForwards(player)) {
            player.moveRelative(1, new Vec3d(0, 0, calculateSpeedForward(player.posY, player.getEntityWorld().getSeaLevel())));
        }
        if (InputHandler.isHoldingBackwards(player)) {
            player.moveRelative(1, new Vec3d(0, 0, -calculateSpeedForward(player.posY, player.getEntityWorld().getSeaLevel()) * 0.8F));
        }
        if (InputHandler.isHoldingLeft(player)) {
            player.moveRelative(1, new Vec3d(calculateSpeedForward(player.posY, player.getEntityWorld().getSeaLevel()), 0, 0));
        }
        if (InputHandler.isHoldingRight(player)) {
            player.moveRelative(1, new Vec3d(-calculateSpeedForward(player.posY, player.getEntityWorld().getSeaLevel()), 0, 0));
        }

        if (InputHandler.isHoldingUp(player)) {
            player.moveRelative(1, new Vec3d(0, (double) 1 / 15, 0));
        }
        else if (InputHandler.isHoldingDown(player)) {
            player.moveRelative(1, new Vec3d(0, (double) -1 / 15, 0));
        }
        else {
            final Vec3d motion = player.getMotion();
            player.setMotion(motion.getX(), 0, motion.getZ());
        }

        if (!player.getEntityWorld().isRemote()) {
            player.fallDistance = 0.0F;
        }
    }


    private static double calculateSpeedForward(double posY, int seaLevel) {
        double baseSpeed = 4.137 / 20;
        double speedMultiplier = 1.5;
        double maxChange = 1.4;
        int astralIslandsHeight = 192;
        int distanceBetweenSeaAndAstralIslands = astralIslandsHeight - seaLevel;
        double percentageDistance = (posY - seaLevel) / distanceBetweenSeaAndAstralIslands;
        if (percentageDistance <= 0) {
            return baseSpeed * speedMultiplier;
        }
        else if (percentageDistance > 1) {
            speedMultiplier = 1;
            return baseSpeed * speedMultiplier;
        }
        else {
            speedMultiplier -= percentageDistance * maxChange;
            return baseSpeed * speedMultiplier;
        }
    }
}
