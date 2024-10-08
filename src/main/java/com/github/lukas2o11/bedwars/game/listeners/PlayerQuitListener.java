package com.github.lukas2o11.bedwars.game.listeners;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.exceptions.EmptyGameStateException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerQuitListener implements Listener {

    private final BedWarsGame game;

    public PlayerQuitListener(final BedWarsGame game) {
        this.game = game;
        game.getBedWars().getServer().getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        game.getUserRegistry().removeUser(event.getPlayer().getUniqueId());
        game.getGameState().orElseThrow(() -> new EmptyGameStateException("Player quit error: GameState in BedWarsGame is empty")).check();
    }
}
