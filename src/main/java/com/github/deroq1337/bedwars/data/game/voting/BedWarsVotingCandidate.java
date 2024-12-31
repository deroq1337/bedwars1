package com.github.deroq1337.bedwars.data.game.voting;

import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface BedWarsVotingCandidate<T> {

    @NotNull T getValue();

    @NotNull BedWarsVotingVotes getVotes();

    @NotNull ItemStack getDisplayItem(@NotNull BedWarsUser user);

    default @NotNull String getDisplayTitle(@NotNull BedWarsUser user) {
        return Optional.ofNullable(getDisplayItem(user).getItemMeta())
                .map(ItemMeta::getDisplayName)
                .orElse("N/A");
    }
}
