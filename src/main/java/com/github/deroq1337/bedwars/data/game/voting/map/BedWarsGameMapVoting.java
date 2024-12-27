package com.github.deroq1337.bedwars.data.game.voting.map;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.exceptions.GameVotingInitializationException;
import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVoting;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVotingCandidate;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class BedWarsGameMapVoting extends BedWarsGameVoting<BedWarsGameMap, BedWarsGameMapVotingCandidate> {

    private static final int RANDOM_MAPS_COUNT = 2;

    public BedWarsGameMapVoting(@NotNull BedWarsGame game) {
        super(game, "Map-Voting", game.getGameMapManager().getRandomMaps(RANDOM_MAPS_COUNT).join().stream()
                        .map(BedWarsGameMapVotingCandidate::new)
                        .toList(),
                4, "§8Map-Voting", new int[]{3, 5}
        );

        int candidatesSize = getCandidates().size();
        if (candidatesSize != RANDOM_MAPS_COUNT) {
            throw new GameVotingInitializationException("Expected " + RANDOM_MAPS_COUNT + " map candidates, but found " + candidatesSize);
        }
    }

    @Override
    public @NotNull ItemStack getDisplayItem() {
        return ItemBuilders.normal(Material.MAP)
                .title("§cMap-Voting")
                .lore("§7Aktueller Gewinner: " + getWinnerName())
                .build();
    }

    private @NotNull String getWinnerName() {
        return getCurrentWinner()
                .map(BedWarsGameVotingCandidate::getDisplayTitle)
                .orElse("N/A");
    }
}
