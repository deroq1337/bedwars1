package com.github.deroq1337.bedwars.data.game.voting;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BedWarsVotingVotes {

    private final @NotNull Set<UUID> votes = new HashSet<>();

    public boolean add(@NotNull UUID uuid) {
        return votes.add(uuid);
    }

    public boolean remove(@NotNull UUID uuid) {
        return votes.remove(uuid);
    }

    public boolean contains(@NotNull UUID uuid) {
        return votes.contains(uuid);
    }

    public @NotNull Set<UUID> get() {
        return votes;
    }

    public int size() {
        return votes.size();
    }
}
