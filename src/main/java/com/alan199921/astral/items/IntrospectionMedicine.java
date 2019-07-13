package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.dimensions.ModDimensions;
import com.alan199921.astral.dimensions.TeleportationTools;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
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
        if(!worldIn.isRemote && entityLiving.dimension != DimensionType.byName(ModDimensions.INNER_REALM)) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) entityLiving;
//            IInnerRealmTeleporter teleporterCapability = worldIn.getCapability(InnerRealmTeleporterProvider.TELEPORTER_CAPABILITY, null).orElse(null);
//            teleporterCapability.teleport(playerEntity, ModDimensions.INNER_REALM_DIMENSION);
            TeleportationTools.changeDim(playerEntity, playerEntity.getPosition(), DimensionType.byName(ModDimensions));
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

}
