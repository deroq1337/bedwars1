package com.github.lukas2o11.bedwars.game.voting.freezer;

import com.github.lukas2o11.bedwars.game.voting.BedWarsGameVoting;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
public class BedWarsGameFreezerVoting implements BedWarsGameVoting<Boolean, BedWarsGameFreezerVotingCandidate> {

    @NotNull
    private final List<BedWarsGameFreezerVotingCandidate> candidates;

    @NotNull
    private final Material displayItem;

    @NotNull
    private final String displayTitle;

    private final int slot;

    public BedWarsGameFreezerVoting() {
        this.candidates = Arrays.asList(
                new BedWarsGameFreezerVotingCandidate(true, Material.GREEN_DYE, 3),
                new BedWarsGameFreezerVotingCandidate(false, Material.RED_DYE, 5)
        );
        this.displayItem = Material.ICE;
        this.displayTitle = "§bFreezer-Voting";
        this.slot = 6;
    }
}
