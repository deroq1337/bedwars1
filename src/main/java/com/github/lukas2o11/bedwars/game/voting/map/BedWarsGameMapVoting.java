package com.github.lukas2o11.bedwars.game.voting.map;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.lukas2o11.bedwars.game.voting.BedWarsGameVoting;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class BedWarsGameMapVoting implements BedWarsGameVoting<DefaultBedWarsGameMap, BedWarsGameMapVotingCandidate> {

    @NotNull
    private final List<BedWarsGameMapVotingCandidate> candidates;

    @NotNull
    private final Material displayItem;

    @NotNull
    private final String displayTitle;

    private final int slot;

    @NotNull
    private final String inventoryTitle;

    public BedWarsGameMapVoting(@NotNull BedWarsGame<DefaultBedWarsGameMap> game) {
        AtomicInteger slot = new AtomicInteger(2);
        this.candidates = game.getGameMapManager().getRandomMaps(3).join().stream()
                .map(map -> new BedWarsGameMapVotingCandidate(map, slot.getAndAdd(2)))
                .toList();
        this.displayItem = Material.FILLED_MAP;
        this.displayTitle = "§cMap-Voting";
        this.slot = 12;
        this.inventoryTitle = "§8Map-Voting";
    }
}
