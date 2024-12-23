package com.github.deroq1337.bedwars.game.voting;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;

public interface BedWarsGameVotingCandidate<T, V extends BedWarsGameVotingVotable<T>> {

    @NotNull V getVotable();

    @NotNull BedWarsGameVotingVotes getVotes();

    default void updateDisplayItem() {
        Optional.ofNullable(getVotable().getDisplayItem().getItemMeta()).ifPresent(itemMeta -> {
            String lore = "§7Votes: §e" + getVotes().get();
            itemMeta.setLore(Collections.singletonList(lore));
            getVotable().getDisplayItem().setItemMeta(itemMeta);
        });
    }
}
