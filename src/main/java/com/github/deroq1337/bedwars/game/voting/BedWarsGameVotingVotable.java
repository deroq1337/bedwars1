package com.github.deroq1337.bedwars.game.voting;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface BedWarsGameVotingVotable<T> {

    @NotNull T getValue();

    @NotNull ItemStack getDisplayItem();

    @NotNull String getDisplayTitle();

    int getSlot();
}
