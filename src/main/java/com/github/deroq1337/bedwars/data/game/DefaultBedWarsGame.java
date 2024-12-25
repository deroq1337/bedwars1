package com.github.deroq1337.bedwars.data.game;

import com.github.deroq1337.bedwars.BedWars;
import com.github.deroq1337.bedwars.data.game.commands.BedWarsMapCommand;
import com.github.deroq1337.bedwars.data.game.commands.BedWarsPauseCommand;
import com.github.deroq1337.bedwars.data.game.commands.BedWarsStartCommand;
import com.github.deroq1337.bedwars.data.game.listeners.InventoryClickListener;
import com.github.deroq1337.bedwars.data.game.listeners.PlayerInteractListener;
import com.github.deroq1337.bedwars.data.game.listeners.PlayerJoinListener;
import com.github.deroq1337.bedwars.data.game.listeners.PlayerQuitListener;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMapManager;
import com.github.deroq1337.bedwars.data.game.map.DefaultBedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.map.DefaultBedWarsGameMapManager;
import com.github.deroq1337.bedwars.data.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyGameState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUserRegistry;
import com.github.deroq1337.bedwars.data.game.user.DefaultBedWarsUserRegistry;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVotingManager;
import com.github.deroq1337.bedwars.data.game.voting.DefaultBedWarsGameVotingManager;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Getter
@Setter
public class DefaultBedWarsGame implements BedWarsGame<DefaultBedWarsGameMap> {

    private final @NotNull BedWars bedWars;
    private final @NotNull BedWarsUserRegistry userRegistry;
    private final @NotNull  BedWarsGameMapManager<DefaultBedWarsGameMap> gameMapManager;
    private final @NotNull BedWarsGameVotingManager<DefaultBedWarsGameMap> gameVotingManager;

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
