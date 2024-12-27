package com.github.deroq1337.bedwars.data.game.listeners;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryClickListener implements Listener {

    private final @NotNull BedWarsGame game;

    public InventoryClickListener(@NotNull BedWarsGame game) {
        this.game = game;
        Bukkit.getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        game.getUserRegistry().getUser(player.getUniqueId()).ifPresentOrElse(user -> {
            if (game.getGameVotingManager().handleInventoryClick(user, event)) {
                return;
            }

            if (game.getGameVotingManager().getVotings().stream()
                    .anyMatch(voting -> voting.handleInventoryClick(user, event))) {
                return;
            }
        }, () -> player.sendMessage("§cAn error occurred. Rejoin or contact an administrator."));
    }
}
