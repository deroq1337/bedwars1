package com.github.deroq1337.bedwars.data.game.voting.map;

import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMap;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsVotingCandidate;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsVotingVotes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class BedWarsGameMapVotingCandidate implements BedWarsVotingCandidate<BedWarsMap> {

    private final @NotNull BedWarsMap value;
    private final @NotNull BedWarsVotingVotes votes = new BedWarsVotingVotes();

    public @NotNull ItemStack getDisplayItem(@NotNull BedWarsUser user) {
        return ItemBuilders.normal(value.getDisplayItem())
                .title("§c" + value.getName())
                .lore(user.getMessage("voting_inventory_candidate_votes", votes.size()))
                .build();
    }
}
