package com.github.lukas2o11.bedwars.game.listeners;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.voting.BedWarsGameVotingManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryClickListener implements Listener {

    //private final BedWarsGame<?> game;
    private final BedWarsGameVotingManager<?> gameVotingManager;

    public InventoryClickListener(@NotNull BedWarsGame game) {
        // this.game = game;
        this.gameVotingManager = game.getGameVotingManager();
        game.getBedWars().getServer().getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        String inventoryTitle = event.getView().getTitle();
        if (inventoryTitle.equals(gameVotingManager.getInventoryTitle())) {
            gameVotingManager.handleInventoryClick(event, (Player) event.getWhoClicked());
            return;
        }

        boolean votingClick = gameVotingManager.getVotings().stream()
                .filter(voting -> voting.getInventoryTitle().equals(inventoryTitle))
                .findFirst()
                .map(voting -> {
                    event.setCancelled(true);
                    voting.handleInventoryClick(event, ((Player) event.getWhoClicked()));
                    return true;
                }).orElse(false);
        if (votingClick) {
            return;
        }
    }
}
