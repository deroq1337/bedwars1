package com.github.deroq1337.bedwars.game.voting.map;

import com.github.deroq1337.bedwars.game.BedWarsGame;
import com.github.deroq1337.bedwars.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.deroq1337.bedwars.game.voting.BedWarsGameVoting;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class BedWarsGameMapVoting implements BedWarsGameVoting<DefaultBedWarsGameMap, BedWarsGameMapVotingVotable, BedWarsGameMapVotingCandidate> {

    private final @NotNull List<BedWarsGameMapVotingCandidate> candidates;
    private final @NotNull ItemStack displayItem;
    private final int slot;
    private final @NotNull String inventoryTitle;

    public BedWarsGameMapVoting(@NotNull BedWarsGame<DefaultBedWarsGameMap> game) {
        AtomicInteger slot = new AtomicInteger(2);
        this.candidates = game.getGameMapManager().getRandomMaps(3).join().stream()
                .map(map -> new BedWarsGameMapVotingCandidate(map, slot.getAndAdd(2)))
                .toList();
        this.displayItem = buildDisplayItem();
        this.slot = 12;
        this.inventoryTitle = "§8Map-Voting";
    }

    @Override
    public @NotNull String getWinnerAsString() {
        return getWinner().map(winner -> winner.getVotable().getValue().getName())
                .orElse("N/A");
    }

    private @NotNull ItemStack buildDisplayItem() {
        return ItemBuilders.normal(Material.FILLED_MAP)
                .title("§cMap-Voting")
                .lore("§7Aktueller Gewinner: §c" + getWinnerAsString())
                .build();
    }
}
