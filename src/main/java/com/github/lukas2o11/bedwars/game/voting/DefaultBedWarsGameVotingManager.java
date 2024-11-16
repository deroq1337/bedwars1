package com.github.lukas2o11.bedwars.game.voting;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.lukas2o11.bedwars.game.voting.freezer.BedWarsGameFreezerVoting;
import com.github.lukas2o11.bedwars.game.voting.freezer.BedWarsGameFreezerVotingCandidate;
import com.github.lukas2o11.bedwars.game.voting.map.BedWarsGameMapVoting;
import com.github.lukas2o11.bedwars.game.voting.map.BedWarsGameMapVotingCandidate;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DefaultBedWarsGameVotingManager implements BedWarsGameVotingManager<DefaultBedWarsGameMap> {

    @NotNull
    private final BedWarsGameVoting<DefaultBedWarsGameMap, BedWarsGameMapVotingCandidate> gameMapVoting;

    @NotNull
    private final BedWarsGameVoting<Boolean, BedWarsGameFreezerVotingCandidate> freezerVoting;

    @NotNull
    @Getter
    private final String inventoryTitle;

    public DefaultBedWarsGameVotingManager(BedWarsGame<DefaultBedWarsGameMap> game) {
        this.gameMapVoting = new BedWarsGameMapVoting(game);
        this.freezerVoting = new BedWarsGameFreezerVoting();
        this.inventoryTitle = "§8Voting";
    }

    @Override
    public @NotNull <C extends BedWarsGameVotingCandidate<DefaultBedWarsGameMap>> BedWarsGameVoting<DefaultBedWarsGameMap, C> getGameMapVoting() {
        return (BedWarsGameVoting<DefaultBedWarsGameMap, C>) gameMapVoting;
    }

    @Override
    public @NotNull <C extends BedWarsGameVotingCandidate<Boolean>> BedWarsGameVoting<Boolean, C> getFreezerVoting() {
        return (BedWarsGameVoting<Boolean, C>) freezerVoting;
    }

    @Override
    public @NotNull List<BedWarsGameVoting<?, ?>> getVotings() {
        return List.of(gameMapVoting, freezerVoting);
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, inventoryTitle);
        Arrays.asList(gameMapVoting, freezerVoting).forEach(voting -> inventory.setItem(voting.getSlot(), new ItemStack(voting.getDisplayItem())));
        return inventory;
    }

    @Override
    public void handleInventoryClick(@NotNull InventoryClickEvent event, @NotNull Player player) {
        event.setCancelled(true);

        Optional.ofNullable(event.getCurrentItem()).ifPresent(item -> {
            BedWarsGameVoting<?, ?> votingByItem = getVotings().stream()
                    .filter(voting -> voting.getDisplayItem() == item.getType())
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No voting found for item '" + item.getType() + "'"));
            player.openInventory(votingByItem.getInventory());
        });
    }
}
