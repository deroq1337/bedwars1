package com.github.deroq1337.bedwars.data.game;

import com.github.deroq1337.bedwars.BedWars;
import com.github.deroq1337.bedwars.data.game.config.MainConfig;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMap;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMapManager;
import com.github.deroq1337.bedwars.data.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.data.game.team.BedWarsTeamManager;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUserRegistry;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsVotingManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface BedWarsGame {

    @NotNull BedWars getBedWars();

    @NotNull MainConfig getMainConfig();

    @NotNull BedWarsUserRegistry getUserRegistry();

    @NotNull BedWarsMapManager getMapManager();

    @NotNull BedWarsVotingManager getVotingManager();

    @NotNull BedWarsTeamManager getTeamManager();

    Optional<BedWarsGameState> getGameState();

    void setGameState(@Nullable BedWarsGameState state);

    Optional<BedWarsMap> getCurrentMap();

    void setCurrentMap(@Nullable BedWarsMap map);

    boolean isForceMapped();

    void forceMap(@NotNull BedWarsMap map);
}
