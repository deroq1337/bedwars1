package com.github.lukas2o11.bedwars.game.voting;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DefaultBedWarsGameVotingVotes implements BedWarsGameVotingVotes {

    @NotNull
    private final Set<UUID> votes = new HashSet<>();

    @Override
    public int get() {
        return votes.size();
    }

    @Override
    public boolean add(UUID player) {
        return votes.add(player);
    }

    @Override
    public void remove(UUID player) {
        votes.remove(player);
    }

    @Override
    public boolean contains(UUID player) {
        return votes.contains(player);
    }
}
