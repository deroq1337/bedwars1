package com.github.deroq1337.bedwars.data.game.voting;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface BedWarsGameVotingVotes {

    int get();

    boolean add(@NotNull UUID player);

    void remove(@NotNull UUID player);

    boolean contains(@NotNull UUID player);
}
