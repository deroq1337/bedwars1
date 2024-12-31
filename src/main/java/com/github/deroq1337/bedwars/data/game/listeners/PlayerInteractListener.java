package com.github.deroq1337.bedwars.data.game.listeners;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import org.bukkit.entity.Player;
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

        Player player = event.getPlayer();
        game.getUserRegistry().getUser(player.getUniqueId()).ifPresentOrElse(user -> {
            Optional.ofNullable(event.getItem()).ifPresent(item -> {
                if (item.isSimilar(game.getVotingManager().getItem(user))) {
                    event.setCancelled(true);
                    event.getPlayer().openInventory(game.getVotingManager().getInventory(user));
                    return;
                }

                if (item.isSimilar(game.getTeamManager().getItem(user))) {
                    event.setCancelled(true);
                    event.getPlayer().openInventory(game.getTeamManager().getInventory(user));
                    return;
                }
            });
        }, () -> player.sendMessage("§cAn error occurred. Rejoin or contact an administrator."));
    }
}
