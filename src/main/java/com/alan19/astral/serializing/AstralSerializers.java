package com.alan19.astral.serializing;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AstralSerializers {
    public static final EntityDataSerializer<Optional<GameProfile>> OPTIONAL_GAME_PROFILE = new EntityDataSerializer<Optional<GameProfile>>() {
        @Override
        public void write(@Nonnull FriendlyByteBuf packetBuffer, @Nonnull Optional<GameProfile> gameProfile) {
            if (gameProfile.isPresent()) {
                packetBuffer.writeBoolean(true);
                packetBuffer.writeNbt(NbtUtils.writeGameProfile(new CompoundTag(), gameProfile.get()));
            }
            else {
                packetBuffer.writeBoolean(false);
            }
        }

        @Override
        @Nonnull
        public Optional<GameProfile> read(@Nonnull FriendlyByteBuf packetBuffer) {
            return packetBuffer.readBoolean() ? Optional.of(NbtUtils.readGameProfile(packetBuffer.readNbt())) : Optional.empty();
        }

        @Override
        @Nonnull
        public Optional<GameProfile> copy(@Nonnull Optional<GameProfile> gameProfile) {
            return gameProfile;
        }
    };

    public static final EntityDataSerializer<LazyOptional<ItemStackHandler>> OPTIONAL_ITEMSTACK_HANDLER = new EntityDataSerializer<LazyOptional<ItemStackHandler>>() {
        @Override
        public void write(@Nonnull FriendlyByteBuf packetBuffer, LazyOptional<ItemStackHandler> itemStackHandlerLazyOptional) {
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
        public LazyOptional<ItemStackHandler> read(FriendlyByteBuf packetBuffer) {
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
