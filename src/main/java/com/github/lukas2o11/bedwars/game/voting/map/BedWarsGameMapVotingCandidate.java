package com.github.lukas2o11.bedwars.game.voting.map;

import com.github.lukas2o11.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.lukas2o11.bedwars.game.voting.BedWarsGameVotingCandidate;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BedWarsGameMapVotingCandidate implements BedWarsGameVotingCandidate<DefaultBedWarsGameMap> {

    @Getter
    private @NotNull final DefaultBedWarsGameMap votable;

    @Getter
    private @NotNull final Material displayItem;

    @Getter
    private final int slot;

    public BedWarsGameMapVotingCandidate(@NotNull DefaultBedWarsGameMap votable, int slot) {
        this.votable = votable;
        this.displayItem = Optional.ofNullable(votable.getDisplayItem())
                .orElse(Material.BEDROCK);
        this.slot = slot;
    }

    private @NotNull final Set<UUID> votes = new HashSet<>();

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
