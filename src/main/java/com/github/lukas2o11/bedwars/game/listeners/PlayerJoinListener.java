package com.github.lukas2o11.bedwars.game.listeners;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.exceptions.EmptyGameStateException;
import com.github.lukas2o11.bedwars.game.user.BedWarsUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    private final BedWarsGame<?> game;

    public PlayerJoinListener(@NotNull BedWarsGame game) {
        this.game = game;
        game.getBedWars().getServer().getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().clear();

        final BedWarsUser user = game.getUserRegistry().addUser(event.getPlayer().getUniqueId(), true);
        user.giveItems();

        game.getGameState().orElseThrow(() -> new EmptyGameStateException("Player join error: GameState in BedWarsGame is empty")).check();
    }
}
