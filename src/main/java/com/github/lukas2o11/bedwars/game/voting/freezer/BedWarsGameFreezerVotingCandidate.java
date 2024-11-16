package com.github.lukas2o11.bedwars.game.voting.freezer;

import com.github.lukas2o11.bedwars.game.voting.BedWarsGameVotingCandidate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class BedWarsGameFreezerVotingCandidate implements BedWarsGameVotingCandidate<Boolean> {

    @NotNull
    @Getter
    private final Boolean votable;

    @NotNull
    @Getter
    private final Material displayItem;

    @Getter
    private final int slot;

    @NotNull
    private final Set<UUID> votes = new HashSet<>();

    @Override
    public int getVotes() {
        return votes.size();
    }

    @Override
    public boolean addVote(@NotNull UUID player) {
        return votes.add(player);
    }

    @Override
    public boolean removeVote(@NotNull UUID player) {
        return votes.remove(player);
    }
}

