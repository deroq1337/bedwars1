package com.github.deroq1337.bedwars.data.game.voting;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface BedWarsGameVotingCandidate<T> {

    @NotNull T getValue();

    @NotNull BedWarsGameVotingVotes getVotes();

    @NotNull ItemStack getDisplayItem();

    default @NotNull String getDisplayTitle() {
        return Optional.ofNullable(getDisplayItem().getItemMeta())
                .map(ItemMeta::getDisplayName)
                .orElse("N/A");
    }
}
