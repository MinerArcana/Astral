package com.alan19.astral.serializing;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AstralSerializers {
    public static final IDataSerializer<Optional<GameProfile>> OPTIONAL_GAME_PROFILE = new IDataSerializer<Optional<GameProfile>>() {
        @Override
        public void write(@Nonnull PacketBuffer packetBuffer, @Nonnull Optional<GameProfile> gameProfile) {
            if (gameProfile.isPresent()) {
                packetBuffer.writeBoolean(true);
                packetBuffer.writeNbt(NBTUtil.writeGameProfile(new CompoundNBT(), gameProfile.get()));
            }
            else {
                packetBuffer.writeBoolean(false);
            }
        }

        @Override
        @Nonnull
        public Optional<GameProfile> read(@Nonnull PacketBuffer packetBuffer) {
            return packetBuffer.readBoolean() ? Optional.of(NBTUtil.readGameProfile(packetBuffer.readNbt())) : Optional.empty();
        }

        @Override
        @Nonnull
        public Optional<GameProfile> copy(@Nonnull Optional<GameProfile> gameProfile) {
            return gameProfile;
        }
    };

    public static final IDataSerializer<LazyOptional<ItemStackHandler>> OPTIONAL_ITEMSTACK_HANDLER = new IDataSerializer<LazyOptional<ItemStackHandler>>() {
        @Override
        public void write(@Nonnull PacketBuffer packetBuffer, LazyOptional<ItemStackHandler> itemStackHandlerLazyOptional) {
            if (itemStackHandlerLazyOptional.isPresent()) {
                itemStackHandlerLazyOptional.ifPresent(itemStackHandler -> {
                    packetBuffer.writeBoolean(true);
                    packetBuffer.writeNbt(itemStackHandler.serializeNBT());
                });
            }
            else {
                packetBuffer.writeBoolean(false);
            }
        }

        @Nonnull
        @Override
        public LazyOptional<ItemStackHandler> read(PacketBuffer packetBuffer) {
            if (packetBuffer.readBoolean()) {
                final ItemStackHandler itemStackHandler = new ItemStackHandler();
                itemStackHandler.deserializeNBT(packetBuffer.readNbt());
                return LazyOptional.of(() -> itemStackHandler);
            }
            return LazyOptional.empty();
        }

        @Nonnull
        @Override
        public LazyOptional<ItemStackHandler> copy(@Nonnull LazyOptional<ItemStackHandler> itemStackHandlerLazyOptional) {
            return itemStackHandlerLazyOptional;
        }
    };
}
