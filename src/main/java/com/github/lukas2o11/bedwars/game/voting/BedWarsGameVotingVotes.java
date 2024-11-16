package com.github.lukas2o11.bedwars.game.voting;

import java.util.UUID;

public interface BedWarsGameVotingVotes {

    int get();

    boolean add(UUID player);

    void remove(UUID player);

    boolean contains(UUID player);
}
