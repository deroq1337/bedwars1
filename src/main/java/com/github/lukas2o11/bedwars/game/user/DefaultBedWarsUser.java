package com.github.lukas2o11.bedwars.game.user;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.exceptions.EmptyGameStateException;
import com.github.lukas2o11.bedwars.game.item.Items;
import com.github.lukas2o11.bedwars.game.state.BedWarsGameState;
import com.github.lukas2o11.bedwars.game.state.BedWarsLobbyGameState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@Setter
public class DefaultBedWarsUser implements BedWarsUser {

    private @NotNull final BedWarsGame<?> game;
    private @NotNull final UUID uuid;

    @Getter
    private boolean alive;

    private DefaultBedWarsUser(@NotNull BedWarsGame game, @NotNull UUID uuid, boolean alive) {
        this.game = game;
        this.uuid = uuid;
        this.alive = alive;
    }

    public static @NotNull DefaultBedWarsUser create(@NotNull BedWarsGame game, @NotNull UUID uuid, boolean alive) {
        return new DefaultBedWarsUser(game, uuid, alive);
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    @Override
    public void giveItems() {
        final BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Error on player join: GameState is null"));
        if (gameState instanceof BedWarsLobbyGameState) {
            Optional.ofNullable(getPlayer()).ifPresent(player -> {
                player.getInventory().setItem(0, Items.TEAM_SELECTOR_ITEM);
                player.getInventory().setItem(4, Items.VOTING_ITEM_ITEM);
                player.getInventory().setItem(7, Items.ACHIEVEMENT_ITEM);
                player.getInventory().setItem(8, Items.LOBBY_ITEM);
            });
        }
    }

    private Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
