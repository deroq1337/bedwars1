package com.github.lukas2o11.bedwars.game.voting;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface BedWarsGameVotingCandidate<T> {

    @NotNull T getVotable();

    @NotNull Material getDisplayItem();

    int getSlot();

    int getVotes();

    boolean addVote(@NotNull UUID player);

    boolean removeVote(@NotNull UUID player);
}
