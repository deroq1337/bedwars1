package com.github.lukas2o11.bedwars.game.voting;

import com.github.lukas2o11.bedwars.game.map.BedWarsGameMap;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public interface BedWarsGameVotingManager<M extends BedWarsGameMap> {

    < C extends BedWarsGameVotingCandidate<M>> @NotNull BedWarsGameVoting<M, C> getGameMapVoting();

    <C extends BedWarsGameVotingCandidate<Boolean>> @NotNull BedWarsGameVoting<Boolean, C> getFreezerVoting();

    @NotNull Inventory getInventory();
}
