package com.alan19.astral.blocks.tileentities;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstralTiles {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Astral.MOD_ID);

    public static final RegistryObject<BlockEntityType<OfferingBrazierTileEntity>> OFFERING_BRAZIER = TILE_ENTITIES.register("offering_brazier_tile", () -> BlockEntityType.Builder.of(OfferingBrazierTileEntity::new, AstralBlocks.OFFERING_BRAZIER.get()).build(null));
    public static final RegistryObject<BlockEntityType<EtherealMobSpawnerTileEntity>> ETHEREAL_MOB_SPAWNER = TILE_ENTITIES.register("ethereal_mob_spawner", () -> BlockEntityType.Builder.of(EtherealMobSpawnerTileEntity::new, AstralBlocks.ETHEREAL_SPAWNER.get()).build(null));

    public static void register(IEventBus modBus) {
        TILE_ENTITIES.register(modBus);
    }
}
