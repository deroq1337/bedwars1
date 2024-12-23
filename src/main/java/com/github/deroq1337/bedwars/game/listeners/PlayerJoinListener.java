package com.github.deroq1337.bedwars.game.listeners;

import com.github.deroq1337.bedwars.game.BedWarsGame;
import com.github.deroq1337.bedwars.game.exceptions.EmptyGameStateException;
import com.github.deroq1337.bedwars.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.game.user.BedWarsUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    private final @NotNull BedWarsGame<?> game;

    public PlayerJoinListener(@NotNull BedWarsGame game) {
        this.game = game;
        game.getBedWars().getServer().getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BedWarsUser user = game.getUserRegistry().addUser(event.getPlayer().getUniqueId(), true);
        BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Player join error: GameState in BedWarsGame is empty"));
        gameState.onJoin(user);
        gameState.check();
    }
}
