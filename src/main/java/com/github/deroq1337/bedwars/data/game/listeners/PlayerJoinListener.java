package com.github.deroq1337.bedwars.data.game.listeners;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.exceptions.EmptyGameStateException;
import com.github.deroq1337.bedwars.data.game.state.BedWarsGameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    private final @NotNull BedWarsGame game;

    public PlayerJoinListener(@NotNull BedWarsGame game) {
        this.game = game;
        game.getBedWars().getServer().getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Player join error: No GameState found"));
        gameState.onJoin(event.getPlayer().getUniqueId());
        gameState.check();
    }
}
