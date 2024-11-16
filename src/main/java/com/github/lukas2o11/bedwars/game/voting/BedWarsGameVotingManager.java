package com.github.lukas2o11.bedwars.game.voting;

import com.github.lukas2o11.bedwars.game.map.BedWarsGameMap;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BedWarsGameVotingManager<M extends BedWarsGameMap> {

    <C extends BedWarsGameVotingCandidate<M>> @NotNull BedWarsGameVoting<M, C> getGameMapVoting();

    <C extends BedWarsGameVotingCandidate<Boolean>> @NotNull BedWarsGameVoting<Boolean, C> getFreezerVoting();

    @NotNull List<BedWarsGameVoting<?, ?>> getVotings();

    @NotNull Inventory getInventory();

    @NotNull String getInventoryTitle();

    void handleInventoryClick(@NotNull InventoryClickEvent event, @NotNull Player player);
}
