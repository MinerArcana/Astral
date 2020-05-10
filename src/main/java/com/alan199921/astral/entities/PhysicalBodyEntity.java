package com.alan199921.astral.entities;

import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.api.bodylink.BodyInfo;
import com.alan199921.astral.api.bodylink.IBodyLinkCapability;
import com.alan199921.astral.configs.AstralConfig;
import com.alan199921.astral.serializing.AstralSerializers;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

public class PhysicalBodyEntity extends LivingEntity {
    private LazyOptional<IBodyLinkCapability> bodyLink = LazyOptional.empty();
    private static final DataParameter<Optional<GameProfile>> gameProfile = EntityDataManager.createKey(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_GAME_PROFILE);
    private static final DataParameter<Boolean> faceDown = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> hungerLevel = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<LazyOptional<ItemStackHandler>> armorInventory = EntityDataManager.createKey(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_ITEMSTACK_HANDLER);
    private static final DataParameter<LazyOptional<ItemStackHandler>> handsInventory = EntityDataManager.createKey(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_ITEMSTACK_HANDLER);

    protected PhysicalBodyEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    private LazyOptional<ItemStackHandler> getArmor() {
        return dataManager.get(armorInventory);
    }

    private LazyOptional<ItemStackHandler> getHands() {
        return dataManager.get(handsInventory);
    }

    @Nonnull
    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        NonNullList<ItemStack> itemStackList = NonNullList.withSize(4, ItemStack.EMPTY);
        int bound = itemStackList.size();
        IntStream.range(0, bound).forEach(i -> itemStackList.set(i, getArmor().orElseGet(() -> new ItemStackHandler(4)).getStackInSlot(i)));
        return itemStackList;
    }

    @Nonnull
    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        switch (slotIn.getSlotType()) {
            case HAND:
                return getHands().map(itemStackHandler -> itemStackHandler.getStackInSlot(slotIn.getIndex())).orElseGet(() -> ItemStack.EMPTY);
            case ARMOR:
                return getArmor().map(itemStackHandler -> itemStackHandler.getStackInSlot(slotIn.getIndex())).orElseGet(() -> ItemStack.EMPTY);
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);

        dataManager.set(gameProfile, !compound.getBoolean("gameProfileExists") ? Optional.empty() : Optional.of(NBTUtil.readGameProfile(compound.getCompound("gameProfile"))));
        dataManager.set(faceDown, compound.getBoolean("facedown"));
        if (!world.isRemote() && world instanceof ServerWorld && getGameProfile().isPresent()) {
            final UUID playerId = getGameProfile().get().getId();
            bodyLink = AstralAPI.getBodyLinkCapability((ServerWorld) world);
            dataManager.set(armorInventory, AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalArmor()));
            dataManager.set(handsInventory, AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalHands()));
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putBoolean("gameProfileExists", dataManager.get(gameProfile).isPresent());
        if (getGameProfile().isPresent()) {
            compound.put("gameProfile", NBTUtil.writeGameProfile(new CompoundNBT(), dataManager.get(gameProfile).get()));
        }
        compound.putBoolean("faceDown", dataManager.get(faceDown));

        super.writeAdditional(compound);
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        dropPhysicalInventory();
    }

    private void clearPhysicalInventory() {
        if (world instanceof ServerWorld && getGameProfile().isPresent()) {
            AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(getGameProfile().get().getId())).ifPresent(psychicInventoryInstance -> {
                for (int i = 0; i < psychicInventoryInstance.getPhysicalInventory().getSlots(); i++) {
                    psychicInventoryInstance.getPhysicalInventory().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalArmor().getSlots(); i++) {
                    psychicInventoryInstance.getPhysicalArmor().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalHands().getSlots(); i++) {
                    psychicInventoryInstance.getPhysicalHands().setStackInSlot(i, ItemStack.EMPTY);
                }
            });
        }
    }


    private void dropPhysicalInventory() {
        if (world instanceof ServerWorld && getGameProfile().isPresent()) {
            AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(getGameProfile().get().getId())).ifPresent(psychicInventoryInstance -> {
                for (int i = 0; i < psychicInventoryInstance.getPhysicalInventory().getSlots(); i++) {
                    Block.spawnAsEntity(world, getPosition(), psychicInventoryInstance.getPhysicalInventory().getStackInSlot(i));
                    psychicInventoryInstance.getPhysicalInventory().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalArmor().getSlots(); i++) {
                    Block.spawnAsEntity(world, getPosition(), psychicInventoryInstance.getPhysicalArmor().getStackInSlot(i));
                    psychicInventoryInstance.getPhysicalArmor().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalHands().getSlots(); i++) {
                    Block.spawnAsEntity(world, getPosition(), psychicInventoryInstance.getPhysicalHands().getStackInSlot(i));
                    psychicInventoryInstance.getPhysicalHands().setStackInSlot(i, ItemStack.EMPTY);
                }
            });
        }
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, @Nonnull ItemStack stack) {
        if (slotIn.getSlotType() == EquipmentSlotType.Group.HAND) {
            getHands().orElseGet(() -> new ItemStackHandler(2)).setStackInSlot(slotIn.getIndex(), stack);
        }
        else if (slotIn.getSlotType() == EquipmentSlotType.Group.ARMOR) {
            getArmor().orElseGet(() -> new ItemStackHandler(4)).setStackInSlot(slotIn.getIndex(), stack);
        }
    }

    @Nonnull
    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    public void setName(String name) {
        this.setCustomName(new StringTextComponent(name + "'s Body"));
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(gameProfile, Optional.empty());
        // Approximately 5% of the storage mobs will be facedown
        dataManager.register(faceDown, Math.random() < .05);
        dataManager.register(hungerLevel, 20F);
        dataManager.register(armorInventory, LazyOptional.empty());
        dataManager.register(handsInventory, LazyOptional.empty());
    }

    /**
     * The entity will update the body link capability every 20 ticks (if it exists), which will then update the player's information
     */
    @Override
    public void tick() {
        if (!world.isRemote() && world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.forceChunk(this.chunkCoordX, this.chunkCoordZ, true);
            if (world.getGameTime() % AstralConfig.getTravelingSettings().getSyncInterval() == 0 && isAlive()) {
                setBodyLinkInfo(serverWorld);
            }
        }
        super.tick();
    }

    public void setBodyLinkInfo(ServerWorld serverWorld) {
        if (getGameProfile().isPresent()) {
            bodyLink.ifPresent(iBodyLinkCapability -> iBodyLinkCapability.setInfo(getGameProfile().get().getId(), new BodyInfo(getHealth(), getPosition(), isAlive(), dimension, getUniqueID()), serverWorld));
        }
    }

    public boolean isFaceDown() {
        return dataManager.get(faceDown);
    }

    public Optional<GameProfile> getGameProfile() {
        return dataManager.get(gameProfile);
    }

    public void setGameProfile(GameProfile playerProfile) {
        dataManager.set(gameProfile, Optional.of(playerProfile));
        if (!world.isRemote() && world instanceof ServerWorld && getGameProfile().isPresent()) {
            final UUID playerId = getGameProfile().get().getId();
            bodyLink = AstralAPI.getBodyLinkCapability((ServerWorld) world);
            bodyLink.ifPresent(iBodyLinkCapability -> iBodyLinkCapability.setInfo(playerId, new BodyInfo(getHealth(), getPosition(), isAlive(), dimension, getUniqueID()), (ServerWorld) world));
            dataManager.set(armorInventory, AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalArmor()));
            dataManager.set(handsInventory, AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalHands()));
        }
    }

    public float getHungerLevel() {
        return dataManager.get(hungerLevel);
    }

    public void setHungerLevel(float hunger) {
        dataManager.set(hungerLevel, hunger);
    }

    /**
     * Kill player if the cause of death is not falling out of the world
     *
     * @param cause The cause of death
     */
    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        if (!world.isRemote() && getGameProfile().isPresent()) {
            PlayerEntity playerEntity = world.getPlayerByUuid(getGameProfile().get().getId());
            //If body is killed, drop inventory and kill the player
            if (!cause.getDamageType().equals("outOfWorld") && playerEntity != null && getGameProfile().isPresent()) {
                super.onDeath(cause);
                setBodyLinkInfo((ServerWorld) world);
            }
            //If body despawns because Astral Travel ends, clear the inventory so nothing gets dropped while inventory gets transferred to player
            else {
                clearPhysicalInventory();
                dataManager.set(armorInventory, LazyOptional.of(() -> new ItemStackHandler(4)));
                dataManager.set(handsInventory, LazyOptional.of(() -> new ItemStackHandler(2)));
                super.onDeath(cause);
            }
        }
    }
}
