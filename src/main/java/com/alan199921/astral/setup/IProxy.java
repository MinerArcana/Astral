package com.alan199921.astral.setup;

import net.minecraft.world.World;

public interface IProxy {

    void init();

    World getClientWorld();
}
