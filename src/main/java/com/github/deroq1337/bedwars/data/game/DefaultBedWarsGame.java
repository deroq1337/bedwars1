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
import com.github.deroq1337.bedwars.data.game.map.BedWarsMapManager;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMap;
import com.github.deroq1337.bedwars.data.game.map.DefaultBedWarsMapManager;
import com.github.deroq1337.bedwars.data.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.team.BedWarsTeamManager;
import com.github.deroq1337.bedwars.data.game.team.DefaultBedWarsTeamManager;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUserRegistry;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsVotingManager;
import com.github.deroq1337.bedwars.data.game.voting.DefaultBedWarsVotingManager;
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
    private final @NotNull BedWarsUserRegistry userRegistry;
    private final @NotNull BedWarsMapManager mapManager;
    private final @NotNull BedWarsVotingManager votingManager;
    private final @NotNull BedWarsTeamManager teamManager;

    private Optional<BedWarsGameState> gameState = Optional.empty();
    private Optional<BedWarsMap> currentMap = Optional.empty();
    private boolean forceMapped = false;

    public DefaultBedWarsGame(@NotNull BedWars bedWars) {
        this.bedWars = bedWars;
        this.mainConfig = new MainConfig();
        this.userRegistry = new BedWarsUserRegistry(this);
        this.mapManager = new DefaultBedWarsMapManager(this);
        this.votingManager = new DefaultBedWarsVotingManager(this);
        this.teamManager = new DefaultBedWarsTeamManager(this);

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
    public void setCurrentMap(@Nullable BedWarsMap map) {
        this.currentMap = Optional.ofNullable(map);
    }

    @Override
    public void forceMap(@NotNull BedWarsMap map) {
        // TODO: load world etc
        this.currentMap = Optional.of(map);
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
