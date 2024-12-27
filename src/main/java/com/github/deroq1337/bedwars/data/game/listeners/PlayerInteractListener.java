package com.github.deroq1337.bedwars.data.game.listeners;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayerInteractListener implements Listener {

    private final @NotNull BedWarsGame game;

    public PlayerInteractListener(@NotNull BedWarsGame game) {
        this.game = game;
        game.getBedWars().getServer().getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Optional.ofNullable(event.getItem()).ifPresent(item -> {
            if (item.isSimilar(game.getGameVotingManager().getVotingItem())) {
                event.setCancelled(true);
                event.getPlayer().openInventory(game.getGameVotingManager().getVotingInventory());
                return;
            }
        });
    }
}
