package com.github.lukas2o11.bedwars.game.listeners;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.exceptions.EmptyGameStateException;
import com.github.lukas2o11.bedwars.game.item.Items;
import com.github.lukas2o11.bedwars.game.state.BedWarsGameState;
import com.github.lukas2o11.bedwars.game.state.BedWarsLobbyGameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractListener implements Listener {

    private final BedWarsGame<?> game;

    public PlayerInteractListener(@NotNull BedWarsGame game) {
        this.game = game;
        game.getBedWars().getServer().getPluginManager().registerEvents(this, game.getBedWars());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Error on interact: GameState is null"));
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() == null) {
                return;
            }

            if (event.getItem().equals(Items.VOTING_ITEM_ITEM)) {
                event.setCancelled(true);
                if (gameState instanceof BedWarsLobbyGameState) {
                    event.getPlayer().openInventory(game.getGameVotingManager().getInventory());
                }
            }
        }
    }
}
