package com.github.lukas2o11.bedwars.game.voting;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface BedWarsGameVotingVotable<T> {

    @NotNull T getValue();

    @NotNull ItemStack getDisplayItem();

    String getDisplayTitle();

    int getSlot();
}
