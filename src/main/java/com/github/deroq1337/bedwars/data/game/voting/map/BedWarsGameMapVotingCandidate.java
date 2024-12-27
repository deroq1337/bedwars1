package com.github.deroq1337.bedwars.data.game.voting.map;

import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVotingCandidate;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVotingVotes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class BedWarsGameMapVotingCandidate implements BedWarsGameVotingCandidate<BedWarsGameMap> {

    private final @NotNull BedWarsGameMap value;
    private final @NotNull BedWarsGameVotingVotes votes = new BedWarsGameVotingVotes();

    public @NotNull ItemStack getDisplayItem(@NotNull BedWarsUser user) {
        return ItemBuilders.normal(value.getDisplayItem())
                .title("§c" + value.getName())
                .lore(user.getMessage("voting_inventory_candidate_votes", votes.size()))
                .build();
    }
}
