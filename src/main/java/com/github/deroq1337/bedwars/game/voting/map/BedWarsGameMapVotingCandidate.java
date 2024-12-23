package com.github.deroq1337.bedwars.game.voting.map;

import com.github.deroq1337.bedwars.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.deroq1337.bedwars.game.voting.BedWarsGameVotingCandidate;
import com.github.deroq1337.bedwars.game.voting.BedWarsGameVotingVotes;
import com.github.deroq1337.bedwars.game.voting.DefaultBedWarsGameVotingVotes;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class BedWarsGameMapVotingCandidate implements BedWarsGameVotingCandidate<DefaultBedWarsGameMap, BedWarsGameMapVotingVotable> {

    private @NotNull final BedWarsGameMapVotingVotable votable;
    private @NotNull final BedWarsGameVotingVotes votes;

    public BedWarsGameMapVotingCandidate(@NotNull DefaultBedWarsGameMap value, int slot) {
        this.votable = new BedWarsGameMapVotingVotable(value, buildDisplayItem(value), slot);
        this.votes = new DefaultBedWarsGameVotingVotes();
    }

    private @NotNull ItemStack buildDisplayItem(@NotNull DefaultBedWarsGameMap value) {
        return ItemBuilders.normal(Optional.ofNullable(value.getDisplayItem())
                        .orElse(Material.BEDROCK))
                .title("§c" + value.getName())
                .lore("§7Votes: §e" + Optional.ofNullable(votes)
                        .map(BedWarsGameVotingVotes::get)
                        .orElse(0))
                .build();
    }
}
