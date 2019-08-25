package com.alan199921.astral.setup;

import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.entities.PhysicalBodyEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy {
    @Override
    public void init() {
        RenderingRegistry.registerEntityRenderingHandler(PhysicalBodyEntity.class, (IRenderFactory<Entity>) manager -> new PhysicalBodyEntityRenderer(manager, new PlayerModel(0.5f, false), 0.5f));
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }
}
