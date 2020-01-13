package com.alan199921.astral.flight;

import com.alan199921.astral.configs.AstralConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FlightHandler {
    /**
     * Handles the movement of the player when they have Astral travel
     *
     * @param player The player whose movement will be handled
     */
    public static void handleAstralFlight(PlayerEntity player) {
        //Gets closest block under player
        int closestY = getClosestBlockUnderPlayer(player);
        MovementType movementType = determineMovementType(player);
        //Move player based on kets pressed
        if (InputHandler.isHoldingForwards(player)) {
            player.moveRelative(1, new Vec3d(0, 0, calculateSpeedForward(player.posY, closestY, movementType)));
        }
        if (InputHandler.isHoldingBackwards(player)) {
            player.moveRelative(1, new Vec3d(0, 0, -calculateSpeedForward(player.posY, closestY, movementType) * 0.8F));
        }
        if (InputHandler.isHoldingLeft(player)) {
            player.moveRelative(1, new Vec3d(calculateSpeedForward(player.posY, closestY, movementType), 0, 0));
        }
        if (InputHandler.isHoldingRight(player)) {
            player.moveRelative(1, new Vec3d(-calculateSpeedForward(player.posY, closestY, movementType), 0, 0));
        }

        if (InputHandler.isHoldingUp(player)) {
            player.moveRelative(1, new Vec3d(0, (double) 1 / 15, 0));
        }
        else if (InputHandler.isHoldingDown(player)) {
            player.moveRelative(1, new Vec3d(0, (double) -1 / 15, 0));
        }
        else {
            //Smooth flying up and down
            final Vec3d motion = player.getMotion();
            player.setMotion(motion.getX(), 0, motion.getZ());
        }

        //Disable fall damage sounds
        if (!player.getEntityWorld().isRemote()) {
            player.fallDistance = 0.0F;
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
        double baseSpeed = AstralConfig.getFlightSettings().getBaseSpeed() / 80;
        if (movementType == MovementType.SNEAKING) {
            return 0.0;
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
