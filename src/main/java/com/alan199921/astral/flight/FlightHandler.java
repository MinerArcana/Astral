package com.alan199921.astral.flight;

import com.alan199921.astral.configs.AstralConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FlightHandler {

    private static Vec3d getAbsoluteMotion(Vec3d relative, float facing) {
        double d0 = relative.lengthSquared();
        if (d0 < 1.0E-7D) {
            return Vec3d.ZERO;
        }
        else {
            Vec3d vec3d = (d0 > 1.0D ? relative.normalize() : relative).scale(1);
            float f = MathHelper.sin(facing * ((float) Math.PI / 180F));
            float f1 = MathHelper.cos(facing * ((float) Math.PI / 180F));
            return new Vec3d(vec3d.x * (double) f1 - vec3d.z * (double) f, vec3d.y, vec3d.z * (double) f1 + vec3d.x * (double) f);
        }
    }

    public static void moveEntity(Entity entity, Vec3d vec3d) {
        Vec3d adjustedVec = getAbsoluteMotion(vec3d, entity.rotationYaw);
        entity.setMotion(adjustedVec);
    }


    /**
     * Handles the movement of the player when they have Astral travel
     *
     * @param player The player whose movement will be handled
     */
    public static void handleAstralFlight(PlayerEntity player) {
        //Gets closest block under player
        int closestY = getClosestBlockUnderPlayer(player);
        MovementType movementType = determineMovementType(player);
        Vec3d nextMovement = new Vec3d(0, 0, 0);
        //Move player based on keys pressed
        if (!(InputHandler.isHoldingForwards(player) && InputHandler.isHoldingBackwards(player))) {
            if (InputHandler.isHoldingForwards(player)) {
                nextMovement = nextMovement.add(0, 0, calculateSpeedForward(player.posY, closestY, movementType));
            }
            if (InputHandler.isHoldingBackwards(player)) {
                nextMovement = nextMovement.add(new Vec3d(0, 0, -calculateSpeedForward(player.posY, closestY, movementType) * 0.8F));
            }
        }
        if (InputHandler.isHoldingLeft(player)) {
            nextMovement = nextMovement.add(new Vec3d(calculateSpeedForward(player.posY, closestY, movementType), 0, 0));
        }
        if (InputHandler.isHoldingRight(player)) {
            nextMovement = nextMovement.add(new Vec3d(-calculateSpeedForward(player.posY, closestY, movementType), 0, 0));
        }
        if (InputHandler.isHoldingUp(player)) {
            nextMovement = nextMovement.add(new Vec3d(0, 1, 0));
        }
        else if (InputHandler.isHoldingDown(player)) {
            nextMovement = nextMovement.add(new Vec3d(0, -1, 0));
        }
        else {
            //Smooth flying up and down
            Vec3d motion = player.getMotion();
            player.setMotion(new Vec3d(motion.getX(), 0, motion.getZ()));
        }

        //Only set velocity when player is pressing a key
        if (!nextMovement.equals(new Vec3d(0, 0, 0))) {
            moveEntity(player, nextMovement);
        }
    }

    private static MovementType determineMovementType(PlayerEntity player) {
        if (player.isSneaking()) {
            return MovementType.SNEAKING;
        }
        else if (player.isSprinting()) {
            return MovementType.SPRINTING;
        }
        else {
            return MovementType.WALKING;
        }
    }

    /**
     * Calculates the speed of the player based on their height and the height of the closest block underneath them
     *
     * @param posY         The player's height
     * @param closestBlock The height of the closest block under the player
     * @param movementType How the player is moving (sprinting, walking, sneaking)
     * @return The speed that the player will travel at, in blocks/tick
     */
    private static double calculateSpeedForward(double posY, int closestBlock, MovementType movementType) {
        double baseSpeed = AstralConfig.getFlightSettings().getBaseSpeed() / 20;
        if (movementType == MovementType.SNEAKING) {
            return 0.0655;
        }
        else if (movementType == MovementType.SPRINTING) {
            baseSpeed *= 1.3;
        }
        double speedMultiplier = AstralConfig.getFlightSettings().getMaxMultiplier();
        double maxChange = AstralConfig.getFlightSettings().getMaxPenalty();

        int maxDifference = AstralConfig.getFlightSettings().getHeightPenaltyLimit();
        double difference = Math.min(maxDifference, posY - closestBlock);
        double speedPenalty = (difference / maxDifference) * maxChange;
        return baseSpeed * (speedMultiplier - speedPenalty);
    }

    /**
     * Gets the Y coordinate closest block under the player
     *
     * @param player The player to check
     * @return The Y coordinate of the closest block under the player, or -1 if no blocks are found
     */
    private static int getClosestBlockUnderPlayer(PlayerEntity player) {
        BlockPos.PooledMutableBlockPos pos = BlockPos.PooledMutableBlockPos.retain(player);
        while (pos.getY() >= 0 && player.getEntityWorld().isAirBlock(pos)) {
            pos.move(Direction.DOWN);
        }
        return pos.getY();
    }

    private enum MovementType {
        SPRINTING, WALKING, SNEAKING
    }
}
