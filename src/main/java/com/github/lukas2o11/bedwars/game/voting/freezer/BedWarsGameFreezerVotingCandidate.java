package com.github.lukas2o11.bedwars.game.voting.freezer;

import com.github.lukas2o11.bedwars.game.voting.BedWarsGameVotingCandidate;
import com.github.lukas2o11.bedwars.game.voting.BedWarsGameVotingVotes;
import com.github.lukas2o11.bedwars.game.voting.DefaultBedWarsGameVotingVotes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class BedWarsGameFreezerVotingCandidate implements BedWarsGameVotingCandidate<Boolean, BedWarsGameFreezerVotingVotable> {

    @NotNull
    private final BedWarsGameFreezerVotingVotable votable;

    @NotNull
    private final BedWarsGameVotingVotes votes;

    public BedWarsGameFreezerVotingCandidate(@NotNull Boolean value, @NotNull ItemStack displayItem, int slot) {
        this.votable = new BedWarsGameFreezerVotingVotable(value, displayItem, slot);
        this.votes = new DefaultBedWarsGameVotingVotes();
    }
}

