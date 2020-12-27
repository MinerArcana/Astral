package com.alan19.astral.blocks.tileentities;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralTiles {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Astral.MOD_ID);

    public static final RegistryObject<TileEntityType<OfferingBrazierTileEntity>> OFFERING_BRAZIER = TILE_ENTITIES.register("offering_brazier_tile", () -> TileEntityType.Builder.create(OfferingBrazierTileEntity::new, AstralBlocks.OFFERING_BRAZIER.get()).build(null));
    public static final RegistryObject<TileEntityType<EtherealMobSpawnerTileEntity>> ETHEREAL_MOB_SPAWNER = TILE_ENTITIES.register("ethereal_mob_spawner", () -> TileEntityType.Builder.create(EtherealMobSpawnerTileEntity::new, AstralBlocks.ETHEREAL_SPAWNER.get()).build(null));

    public static void register(IEventBus modBus) {
        TILE_ENTITIES.register(modBus);
    }
}
