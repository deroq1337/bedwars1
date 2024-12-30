package com.github.deroq1337.bedwars.data.game;

import com.github.deroq1337.bedwars.BedWars;
import com.github.deroq1337.bedwars.data.game.commands.BedWarsForceMapCommand;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapCommand;
import com.github.deroq1337.bedwars.data.game.commands.BedWarsPauseCommand;
import com.github.deroq1337.bedwars.data.game.commands.BedWarsStartCommand;
import com.github.deroq1337.bedwars.data.game.config.MainConfig;
import com.github.deroq1337.bedwars.data.game.listeners.InventoryClickListener;
import com.github.deroq1337.bedwars.data.game.listeners.PlayerInteractListener;
import com.github.deroq1337.bedwars.data.game.listeners.PlayerJoinListener;
import com.github.deroq1337.bedwars.data.game.listeners.PlayerQuitListener;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMapManager;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.map.DefaultBedWarsGameMapManager;
import com.github.deroq1337.bedwars.data.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUserRegistry;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVotingManager;
import com.github.deroq1337.bedwars.data.game.voting.DefaultBedWarsGameVotingManager;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Getter
@Setter
public class DefaultBedWarsGame implements BedWarsGame {

    private final @NotNull BedWars bedWars;
    private final @NotNull MainConfig mainConfig;
    private final @NotNull BedWarsGameUserRegistry userRegistry;
    private final @NotNull BedWarsGameMapManager gameMapManager;
    private final @NotNull BedWarsGameVotingManager gameVotingManager;

    private Optional<BedWarsGameState> gameState = Optional.empty();
    private Optional<BedWarsGameMap> gameMap = Optional.empty();
    private boolean forceMapped = false;

    public DefaultBedWarsGame(@NotNull BedWars bedWars) {
        this.bedWars = bedWars;
        this.mainConfig = new MainConfig();
        this.userRegistry = new BedWarsGameUserRegistry(this);
        this.gameMapManager = new DefaultBedWarsGameMapManager(this);
        this.gameVotingManager = new DefaultBedWarsGameVotingManager(this);

        this.gameState = Optional.of(new BedWarsLobbyState(this));
        gameState.get().enter();

        registerListeners();
        registerCommands();
    }

    @Override
    public void setGameState(@Nullable BedWarsGameState state) {
        this.gameState = Optional.ofNullable(state);
    }

    @Override
    public void setGameMap(@Nullable BedWarsGameMap gameMap) {
        this.gameMap = Optional.ofNullable(gameMap);
    }

    @Override
    public void forceMap(@NotNull BedWarsGameMap gameMap) {
        // TODO: load world etc
        this.gameMap = Optional.of(gameMap);
        this.forceMapped = true;
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
        new BedWarsForceMapCommand(this);
    }
}
