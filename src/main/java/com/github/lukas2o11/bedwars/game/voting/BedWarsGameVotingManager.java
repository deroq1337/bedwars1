package com.github.lukas2o11.bedwars.game.voting;

import com.github.lukas2o11.bedwars.game.map.BedWarsGameMap;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface BedWarsGameVotingManager<M extends BedWarsGameMap> {

    <V extends BedWarsGameVotingVotable<M>, C extends BedWarsGameVotingCandidate<M, V>> @NotNull BedWarsGameVoting<M, V, C> getGameMapVoting();

    <V extends BedWarsGameVotingVotable<Boolean>, C extends BedWarsGameVotingCandidate<Boolean, V>> @NotNull BedWarsGameVoting<Boolean, V, C> getFreezerVoting();

   @NotNull List<BedWarsGameVoting<? extends Object,? extends BedWarsGameVotingVotable<? extends Object>,? extends BedWarsGameVotingCandidate<? extends Object,? extends BedWarsGameVotingVotable<? extends Object>>>> getVotings();

    @NotNull Inventory getInventory();

    @NotNull String getInventoryTitle();

    default void handleInventoryClick(@NotNull InventoryClickEvent event, @NotNull Player player) {
        event.setCancelled(true);

        Optional.ofNullable(event.getCurrentItem()).ifPresent(item -> {
            BedWarsGameVoting<?, ?, ?> votingByItem = getVotings().stream()
                    .filter(voting -> voting.getDisplayItem().getType() == item.getType())
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No voting found for item '" + item.getType() + "'"));
            player.openInventory(votingByItem.getInventory());
        });
    }
}
