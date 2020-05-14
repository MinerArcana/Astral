package com.alan199921.astral.api.constructtracker;

import com.alan199921.astral.mentalconstructs.AstralMentalConstructs;
import com.alan199921.astral.mentalconstructs.MentalConstruct;
import com.alan199921.astral.mentalconstructs.MentalConstructType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.RegistryObject;

import java.util.Optional;

public class MentalConstructEntry implements INBTSerializable<CompoundNBT> {
    private ResourceLocation constructWorld;
    private BlockPos constructPos;
    private MentalConstruct mentalConstruct;
    private int level;

    public MentalConstructEntry(MentalConstruct type) {
        mentalConstruct = type;
        level = -1;
    }

    public Optional<ResourceLocation> getConstructWorld() {
        return Optional.ofNullable(constructWorld);
    }

    public void setConstructWorld(World constructWorld) {
        if (constructWorld != null) {
            this.constructWorld = constructWorld.getDimension().getType().getRegistryName();
        }
        else {
            this.constructWorld = null;
        }
    }

    public Optional<BlockPos> getConstructPos() {
        return Optional.ofNullable(constructPos);
    }

    public void setConstructPos(BlockPos constructPos) {
        this.constructPos = constructPos;
    }

    public MentalConstruct getMentalConstruct() {
        return mentalConstruct;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT constructNBT = new CompoundNBT();
        constructNBT.put("constructInfo", mentalConstruct.serializeNBT());
        constructNBT.putInt("level", level);
        constructNBT.putBoolean("posExists", constructPos != null);
        constructNBT.putString("constructType", mentalConstruct.getType().getRegistryName().toString());
        if (constructPos != null) {
            final CompoundNBT blockPosNbt = NBTUtil.writeBlockPos(constructPos);
            constructNBT.put("pos", blockPosNbt);
        }
        constructNBT.putBoolean("worldExists", constructWorld != null);
        if (constructWorld != null) {
            constructNBT.putString("world", constructWorld.toString());
        }
        return constructNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        final String constructName = nbt.getString("constructType");
        final Optional<RegistryObject<MentalConstructType>> optionalConstructEntry = AstralMentalConstructs.MENTAL_CONSTRUCTS.getEntries()
                .stream()
                .filter(constructRegistryObject -> constructRegistryObject.getId().toString().equals(constructName))
                .findFirst();
        optionalConstructEntry.ifPresent(mentalConstructTypeRegistryObject -> mentalConstruct = mentalConstructTypeRegistryObject.get().create());
        if (nbt.getBoolean("worldExists")) {
            constructWorld = new ResourceLocation(nbt.getString("world"));
        }
        if (nbt.getBoolean("posExists")) {
            constructPos = NBTUtil.readBlockPos(nbt.getCompound("pos"));
        }
        level = nbt.getInt("level");
    }
}
