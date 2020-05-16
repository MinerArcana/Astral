package com.alan19.astral.blocks.tileentities;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralTiles {
    @ObjectHolder("astral:offering_brazier_tile")
    public static final TileEntityType<?> OFFERING_BRAZIER_TILE = null;

    @SubscribeEvent
    public static void registerTE(RegistryEvent.Register<TileEntityType<?>> event) {
        TileEntityType<?> offeringBrazierTileEntity = TileEntityType.Builder.create(OfferingBrazierTile::new, AstralBlocks.OFFERING_BRAZIER.get()).build(null);
        offeringBrazierTileEntity.setRegistryName(Astral.MOD_ID, "offering_brazier_tile");
        event.getRegistry().register(offeringBrazierTileEntity);
    }
}
