package com.github.deroq1337.bedwars.game.listeners;

import com.github.deroq1337.bedwars.game.BedWarsGame;
import com.github.deroq1337.bedwars.game.exceptions.EmptyGameStateException;
import com.github.deroq1337.bedwars.game.item.Items;
import com.github.deroq1337.bedwars.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.game.state.BedWarsLobbyGameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayerInteractListener implements Listener {

    private @NotNull final BedWarsGame<?> game;

    public PlayerInteractListener(@NotNull BedWarsGame game) {
        this.game = game;
        game.getBedWars().getServer().getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Error on interact: GameState is null"));
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Optional.ofNullable(event.getItem()).ifPresent(item -> {
                if (item.equals(Items.VOTING_ITEM_ITEM)) {
                    if (gameState instanceof BedWarsLobbyGameState) {
                        event.getPlayer().openInventory(game.getGameVotingManager().getInventory());
                    }
                    event.setCancelled(true);
                    return;
                }
            });
        }
    }
}
