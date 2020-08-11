package com.alan19.astral.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Entity for the Intention Tracker. Based off the Mana Burst from Botania.
 * https://github.com/Vazkii/Botania/blob/eed14c95cccea7c496fe674327d0ec8b0e999cc9/src/main/java/vazkii/botania/common/entity/EntityManaBurst.java#L49
 */
public class IntentionBeam extends ThrowableEntity {
    private static final DataParameter<String> PLAYER_UUID = EntityDataManager.createKey(IntentionBeam.class, DataSerializers.STRING);
    private static final DataParameter<Integer> BEAM_LEVEL = EntityDataManager.createKey(IntentionBeam.class, DataSerializers.VARINT);

    protected IntentionBeam(PlayerEntity playerEntity, EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
    }


    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.BLOCK){
            final BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;

        }
    }

    @Override
    protected void registerData() {
        dataManager.register(PLAYER_UUID, "");
    }

    public void setPlayer(PlayerEntity player){
        dataManager.set(PLAYER_UUID, player.getUniqueID().toString());
    }

    public void setLevel(int level){
        dataManager.set(BEAM_LEVEL, level);
    }
}
