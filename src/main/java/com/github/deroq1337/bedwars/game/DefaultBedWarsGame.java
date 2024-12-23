package com.github.deroq1337.bedwars.game;

import com.github.deroq1337.bedwars.BedWars;
import com.github.deroq1337.bedwars.game.commands.BedWarsMapCommand;
import com.github.deroq1337.bedwars.game.commands.BedWarsPauseCommand;
import com.github.deroq1337.bedwars.game.commands.BedWarsStartCommand;
import com.github.deroq1337.bedwars.game.listeners.InventoryClickListener;
import com.github.deroq1337.bedwars.game.listeners.PlayerInteractListener;
import com.github.deroq1337.bedwars.game.listeners.PlayerJoinListener;
import com.github.deroq1337.bedwars.game.listeners.PlayerQuitListener;
import com.github.deroq1337.bedwars.game.map.BedWarsGameMapManager;
import com.github.deroq1337.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.deroq1337.bedwars.game.map.DefaultBedWarsGameMapManager;
import com.github.deroq1337.bedwars.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.game.state.BedWarsLobbyGameState;
import com.github.deroq1337.bedwars.game.user.BedWarsUserRegistry;
import com.github.deroq1337.bedwars.game.user.DefaultBedWarsUserRegistry;
import com.github.deroq1337.bedwars.game.voting.BedWarsGameVotingManager;
import com.github.deroq1337.bedwars.game.voting.DefaultBedWarsGameVotingManager;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Getter
@Setter
public class DefaultBedWarsGame implements BedWarsGame<DefaultBedWarsGameMap> {

    private @NotNull final BedWars bedWars;
    private @NotNull final BedWarsUserRegistry userRegistry;
    private @NotNull final BedWarsGameMapManager<DefaultBedWarsGameMap> gameMapManager;
    private @NotNull final BedWarsGameVotingManager<DefaultBedWarsGameMap> gameVotingManager;

    private Optional<BedWarsGameState> gameState;
    private Optional<DefaultBedWarsGameMap> gameMap;

    public DefaultBedWarsGame(@NotNull BedWars bedWars) {
        this.bedWars = bedWars;
        this.userRegistry = new DefaultBedWarsUserRegistry();
        this.gameMapManager = new DefaultBedWarsGameMapManager(this);
        this.gameVotingManager = new DefaultBedWarsGameVotingManager(this);

        this.gameState = Optional.of(new BedWarsLobbyGameState(this));
        gameState.get().enter();

        registerListeners();
        registerCommands();
    }

    @Override
    public void setGameState(@Nullable BedWarsGameState state) {
        this.gameState = Optional.ofNullable(state);
    }

    private void registerListeners() {
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);
        new PlayerInteractListener(this);
        new InventoryClickListener(this);
    }

    private void registerCommands() {
        new BedWarsStartCommand(this);
        new BedWarsPauseCommand(this);
        new BedWarsMapCommand(this);
    }
}
