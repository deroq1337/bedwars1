package com.github.deroq1337.bedwars.data.game.voting.map;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMap;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsVoting;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
public class BedWarsGameMapVoting extends BedWarsVoting<BedWarsMap, BedWarsGameMapVotingCandidate> {

    public BedWarsGameMapVoting(@NotNull BedWarsGame game) {
        super(game, new ArrayList<>(), game.getMainConfig().getMapVotingSlot(), game.getMainConfig().getMapVotingInventorySize(), game.getMainConfig().getMapVotingInventorySlots());
        getCandidates().addAll(game.getMapManager().getRandomMaps(getInventorySlots().size()).join().stream()
                .map(BedWarsGameMapVotingCandidate::new)
                .toList());
    }

    @Override
    public @NotNull String getName(@NotNull BedWarsUser user) {
        return user.getMessage("voting_map_name");
    }

    @Override
    public @NotNull String getInventoryTitle(@NotNull BedWarsUser user) {
        return user.getMessage("voting_map_inventory_title");
    }

    @Override
    public @NotNull ItemStack getDisplayItem(@NotNull BedWarsUser user) {
        return ItemBuilders.normal(Material.MAP)
                .title(user.getMessage("voting_map_inventory_item_name"))
                .lore(user.getMessage("voting_map_inventory_current_winner", getWinnerName(user)))
                .build();
    }

    @Override
    public boolean canVote() {
        return super.canVote() && !game.isForceMapped();
    }

    private @NotNull String getWinnerName(@NotNull BedWarsUser user) {
        return getCurrentWinner()
                .map(candidate -> candidate.getDisplayTitle(user))
                .orElse("N/A");
    }
}
