package com.github.deroq1337.bedwars.data.game.listeners;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVotingManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryClickListener implements Listener {

    private final @NotNull BedWarsGameVotingManager<?> gameVotingManager;

    public InventoryClickListener(@NotNull BedWarsGame game) {
        this.gameVotingManager = game.getGameVotingManager();
        game.getBedWars().getServer().getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        String inventoryTitle = event.getView().getTitle();
        if (inventoryTitle.isEmpty()) {
            return;
        }

        if (inventoryTitle.equals(gameVotingManager.getInventoryTitle())) {
            gameVotingManager.handleInventoryClick(event, (Player) event.getWhoClicked());
            return;
        }

        gameVotingManager.getVotings().stream()
                .filter(voting -> voting.getInventoryTitle().equals(inventoryTitle))
                .findFirst()
                .ifPresent(voting -> {
                    event.setCancelled(true);
                    voting.handleInventoryClick(event, ((Player) event.getWhoClicked()));
                });
    }
}
