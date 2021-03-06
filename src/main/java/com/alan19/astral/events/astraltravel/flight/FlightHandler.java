package com.alan19.astral.events.astraltravel.flight;

import com.alan19.astral.api.heightadjustment.HeightAdjustmentCapability;
import com.alan19.astral.api.heightadjustment.HeightAdjustmentProvider;
import com.alan19.astral.api.heightadjustment.IHeightAdjustmentCapability;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.configs.TravelingSettings;
import com.alan19.astral.effects.AstralEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class FlightHandler {

    private static final TravelingSettings travelingSettings = AstralConfig.getTravelingSettings();

    //Copied from Entity.java
    private static Vector3d getAbsoluteMotion(Vector3d relative, float facing) {
        double d0 = relative.lengthSquared();
        if (d0 < 1.0E-7D) {
            return Vector3d.ZERO;
        }
        else {
            Vector3d vec3d = (d0 > 1.0D ? relative.normalize() : relative).scale(1);
            float f = MathHelper.sin(facing * ((float) Math.PI / 180F));
            float f1 = MathHelper.cos(facing * ((float) Math.PI / 180F));
            return new Vector3d(vec3d.x * (double) f1 - vec3d.z * (double) f, vec3d.y, vec3d.z * (double) f1 + vec3d.x * (double) f);
        }
    }

    //Move the player with a certain vector
    public static void moveEntity(Entity entity, Vector3d vec3d) {
        Vector3d adjustedVec = getAbsoluteMotion(vec3d, entity.rotationYaw);
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
        final IHeightAdjustmentCapability heightAdjustmentCapability = player.getCapability(HeightAdjustmentProvider.HEIGHT_ADJUSTMENT_CAPABILITY).orElseGet(HeightAdjustmentCapability::new);
        activateHoverCapability(player, closestY, heightAdjustmentCapability);
        Vector3d nextMovement = generateMovementVector(player, closestY, movementType, heightAdjustmentCapability);
        //Only set velocity when player is pressing a key
        if (!nextMovement.equals(new Vector3d(0, 0, 0))) {
            moveEntity(player, nextMovement);
        }
    }

    /**
     * Generates the movement vector for the player based on their height and keys pressed
     *
     * @param player                     The player that will be moved
     * @param closestY                   The y level of the closest block under the player
     * @param movementType               How the player is moving (Sprint, Sneak)
     * @param heightAdjustmentCapability The player's HeightAdjustmentCapability that stores their target height
     * @return The movement vector to move the player with
     */
    public static Vector3d generateMovementVector(PlayerEntity player, int closestY, MovementType movementType, IHeightAdjustmentCapability heightAdjustmentCapability) {
        Vector3d nextMovement = new Vector3d(0, 0, 0);
        nextMovement = generateCardinalDirectionVector(player, closestY, movementType, nextMovement);
        nextMovement = generateVerticalVector(player, closestY, nextMovement, heightAdjustmentCapability);
        return nextMovement;
    }

    /**
     * Adds the vertical component to the vector
     *
     * @param player                     The player that will be moved
     * @param closestY                   The y level of the closest block under the player
     * @param nextMovement               The vector that is being customized
     * @param heightAdjustmentCapability The player's HeightAdjustmentCapability that stores their target height
     * @return The movement vector with a vertical component
     */
    public static Vector3d generateVerticalVector(PlayerEntity player, int closestY, Vector3d nextMovement, IHeightAdjustmentCapability heightAdjustmentCapability) {
        if (InputHandler.isHoldingUp(player) || (heightAdjustmentCapability.isActive() && heightAdjustmentCapability.getHeightDifference() > Math.floor(player.getPosY()) - closestY && !InputHandler.isHoldingDown(player))) {
            nextMovement = nextMovement.add(new Vector3d(0, heightAdjustmentCapability.isActive() ? getAdjustedVerticalSpeed(heightAdjustmentCapability.getHeightDifference(), player.getPosY() - closestY) : (travelingSettings.baseSpeed.get() / 8), 0))
            ;
        }
        else if (InputHandler.isHoldingDown(player) || (heightAdjustmentCapability.isActive() && heightAdjustmentCapability.getHeightDifference() < Math.floor(player.getPosY()) - closestY && !InputHandler.isHoldingDown(player))) {
            nextMovement = nextMovement.add(new Vector3d(0, heightAdjustmentCapability.isActive() ? -getAdjustedVerticalSpeed(heightAdjustmentCapability.getHeightDifference(), player.getPosY() - closestY) : -(travelingSettings.baseSpeed.get() / 8), 0));
        }
        else {
            //Smooth flying up and down
            Vector3d motion = player.getMotion();
            player.setMotion(new Vector3d(motion.getX(), heightAdjustmentCapability.isActive() ? motion.getY() : 0, motion.getZ()));
        }
        return nextMovement;
    }

    /**
     * Adds the horizontal (XZ) component to the vector
     *
     * @param player       The player to be moved
     * @param closestY     The y coordinate of the closest block under the player
     * @param movementType How the player is moving (sneaking, sprinting)
     * @param nextMovement The vector that is being customized
     * @return The movement vector with a XZ component
     */
    public static Vector3d generateCardinalDirectionVector(PlayerEntity player, int closestY, MovementType movementType, Vector3d nextMovement) {
        final EffectInstance astralTravelInstance = player.getActivePotionEffect(AstralEffects.ASTRAL_TRAVEL.get());
        int amplifier = astralTravelInstance != null ? astralTravelInstance.getAmplifier() : 0;
        if (!(InputHandler.isHoldingForwards(player) && InputHandler.isHoldingBackwards(player))) {
            if (InputHandler.isHoldingForwards(player)) {
                nextMovement = nextMovement.add(0, 0, calculateSpeedForward(player.getPosY(), closestY, movementType, amplifier));
            }
            if (InputHandler.isHoldingBackwards(player)) {
                nextMovement = nextMovement.add(new Vector3d(0, 0, -calculateSpeedForward(player.getPosY(), closestY, movementType, amplifier) * 0.8F));
            }
        }
        if (InputHandler.isHoldingLeft(player)) {
            nextMovement = nextMovement.add(new Vector3d(calculateSpeedForward(player.getPosY(), closestY, movementType, amplifier), 0, 0));
        }
        if (InputHandler.isHoldingRight(player)) {
            nextMovement = nextMovement.add(new Vector3d(-calculateSpeedForward(player.getPosY(), closestY, movementType, amplifier), 0, 0));
        }
        return nextMovement;
    }

    public static void activateHoverCapability(PlayerEntity player, int closestY, IHeightAdjustmentCapability heightAdjustmentCapability) {
        if (player.world.isRemote()) {
            if (InputHandler.isHoldingSprint(player) && !heightAdjustmentCapability.isActive()) {
                heightAdjustmentCapability.activate();
                heightAdjustmentCapability.setHeightDifference((int) Math.min(travelingSettings.heightPenaltyLimit.get(), player.getPosY() - closestY));
            }
            else if (!InputHandler.isHoldingSprint(player) && heightAdjustmentCapability.isActive()) {
                heightAdjustmentCapability.deactivate();
            }
        }
    }

    /**
     * Calculates the vertical speed for the player when they are floating up and down.
     * When floating with automatic height adjustment, speed is reduced by 5 times the regular flying speed.
     * This speed is further reduced for smooth stopping as you get closer to your desired height.
     *
     * @param targetDistance    The target distance from the ground
     * @param heightAboveGround The number of blocks the player is above the closest block under them
     * @return The vertical velocity of the player
     */
    private static double getAdjustedVerticalSpeed(int targetDistance, double heightAboveGround) {
        final double maxFlyingSpeed = travelingSettings.baseSpeed.get() / 40;
        final double deltaHeight = Math.abs(heightAboveGround - targetDistance);
        if (deltaHeight <= travelingSettings.decelerationDistance.get()) {
            double acceleration = -Math.pow(maxFlyingSpeed, 2) / (2 * travelingSettings.decelerationDistance.get());
            return Math.sqrt(Math.pow(maxFlyingSpeed, 2) + (2 * acceleration * deltaHeight));
        }
        return maxFlyingSpeed;
    }

    private static MovementType determineMovementType(PlayerEntity player) {
        if (player.isCrouching()) {
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
     * @param amplifier    The amplifier of the Astral Travel potion effect
     * @return The speed that the player will travel at, in blocks/tick
     */
    private static double calculateSpeedForward(double posY, int closestBlock, MovementType movementType, int amplifier) {
        double baseSpeed = travelingSettings.baseSpeed.get() / 20;
        if (movementType == MovementType.SNEAKING) {
            return 0.0655;
        }
        else if (movementType == MovementType.SPRINTING) {
            baseSpeed *= 1.3;
        }
        double speedMultiplier = travelingSettings.maxMultiplier.get();
        double maxChange = travelingSettings.maxPenalty.get();

        int maxDifference = travelingSettings.heightPenaltyLimit.get();
        double difference = Math.min(maxDifference, posY - closestBlock);
        double speedPenalty = (difference / maxDifference) * maxChange;
        return baseSpeed * (speedMultiplier - speedPenalty) * (1 + amplifier * .5);
    }

    /**
     * Gets the Y coordinate closest block under the player
     *
     * @param player The player to check
     * @return The Y coordinate of the closest block under the player, or -1 if no blocks are found
     */
    private static int getClosestBlockUnderPlayer(PlayerEntity player) {
        BlockPos pos = player.getPosition();
        while (pos.getY() >= 0 && !player.getEntityWorld().getBlockState(pos).hasOpaqueCollisionShape(player.world, pos)) {
            pos = pos.down();
        }
        return pos.getY();
    }

    private enum MovementType {
        SPRINTING, WALKING, SNEAKING
    }
}
