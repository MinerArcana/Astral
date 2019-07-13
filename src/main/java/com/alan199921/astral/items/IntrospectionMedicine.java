package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.capabilities.IInnerRealmTeleporter;
import com.alan199921.astral.capabilities.InnerRealmTeleporterProvider;
import com.alan199921.astral.dimensions.ModDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.checkerframework.checker.nullness.qual.NonNull;

public class IntrospectionMedicine extends Item {
    public IntrospectionMedicine() {
        super(new Properties()
                .food(new Food.Builder()
                        .setAlwaysEdible()
                        .build())
                .group(Astral.setup.astralItems)
        );
        setRegistryName("introspection_medicine");
    }

    @NonNull
    @Override
    public ItemStack onItemUseFinish(@NonNull ItemStack stack, World worldIn, @NonNull LivingEntity entityLiving) {
        if(!worldIn.isRemote && entityLiving.dimension != ModDimensions.INNER_REAML_DIMENSION) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) entityLiving;
            IInnerRealmTeleporter teleporterCapability = worldIn.getCapability(InnerRealmTeleporterProvider.TELEPORTER_CAPABILITY, null).orElse(null);
            teleporterCapability.teleport(playerEntity, ModDimensions.INNER_REAML_DIMENSION);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

}
