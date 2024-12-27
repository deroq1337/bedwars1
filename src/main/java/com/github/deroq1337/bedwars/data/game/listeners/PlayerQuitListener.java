package com.github.deroq1337.bedwars.data.game.listeners;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.exceptions.EmptyGameStateException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {

    private final @NotNull BedWarsGame game;

    public PlayerQuitListener(@NotNull BedWarsGame game) {
        this.game = game;
        game.getBedWars().getServer().getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        game.getUserRegistry().removeUser(event.getPlayer().getUniqueId());
        game.getGameState().orElseThrow(() -> new EmptyGameStateException("Player quit error: No GameState found")).check();
    }
}
