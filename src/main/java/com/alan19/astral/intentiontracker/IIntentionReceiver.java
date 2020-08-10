package com.alan19.astral.intentiontracker;

import net.minecraft.entity.player.PlayerEntity;

public interface IIntentionReceiver {
    void onIntentionReceived(PlayerEntity playerEntity);
}
