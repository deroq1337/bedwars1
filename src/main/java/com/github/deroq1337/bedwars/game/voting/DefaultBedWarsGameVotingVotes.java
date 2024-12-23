package com.github.deroq1337.bedwars.game.voting;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DefaultBedWarsGameVotingVotes implements BedWarsGameVotingVotes {

    private final @NotNull Set<UUID> votes = new HashSet<>();

    @Override
    public int get() {
        return votes.size();
    }

    @Override
    public boolean add(@NotNull UUID player) {
        return votes.add(player);
    }

    @Override
    public void remove(@NotNull UUID player) {
        votes.remove(player);
    }

    @Override
    public boolean contains(@NotNull UUID player) {
        return votes.contains(player);
    }
}
