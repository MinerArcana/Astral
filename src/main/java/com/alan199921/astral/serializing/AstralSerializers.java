package com.alan199921.astral.serializing;

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
                packetBuffer.writeCompoundTag(NBTUtil.writeGameProfile(new CompoundNBT(), gameProfile.get()));
            }
            else {
                packetBuffer.writeBoolean(false);
            }
        }

        @Override
        @Nonnull
        public Optional<GameProfile> read(@Nonnull PacketBuffer packetBuffer) {
            return packetBuffer.readBoolean() ? Optional.of(NBTUtil.readGameProfile(packetBuffer.readCompoundTag())) : Optional.empty();
        }

        @Override
        @Nonnull
        public Optional<GameProfile> copyValue(@Nonnull Optional<GameProfile> gameProfile) {
            return gameProfile;
        }
    };

    public static final IDataSerializer<LazyOptional<ItemStackHandler>> OPTIONAL_ITEMSTACK_HANDLER = new IDataSerializer<LazyOptional<ItemStackHandler>>() {
        @Override
        public void write(PacketBuffer packetBuffer, LazyOptional<ItemStackHandler> itemStackHandlerLazyOptional) {
            if (itemStackHandlerLazyOptional.isPresent()) {
                itemStackHandlerLazyOptional.ifPresent(itemStackHandler -> {
                    packetBuffer.writeBoolean(true);
                    packetBuffer.writeCompoundTag(itemStackHandler.serializeNBT());
                });
            }
            else {
                packetBuffer.writeBoolean(false);
            }
        }

        @Override
        public LazyOptional<ItemStackHandler> read(PacketBuffer packetBuffer) {
            if (packetBuffer.readBoolean()) {
                final ItemStackHandler itemStackHandler = new ItemStackHandler();
                itemStackHandler.deserializeNBT(packetBuffer.readCompoundTag());
                return LazyOptional.of(() -> itemStackHandler);
            }
            return LazyOptional.empty();
        }

        @Override
        public LazyOptional<ItemStackHandler> copyValue(LazyOptional<ItemStackHandler> itemStackHandlerLazyOptional) {
            return itemStackHandlerLazyOptional;
        }
    };
}
