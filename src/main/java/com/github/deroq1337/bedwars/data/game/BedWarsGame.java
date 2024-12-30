package com.github.deroq1337.bedwars.data.game;

import com.github.deroq1337.bedwars.BedWars;
import com.github.deroq1337.bedwars.data.game.config.MainConfig;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMapManager;
import com.github.deroq1337.bedwars.data.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUserRegistry;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVotingManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface BedWarsGame {

    @NotNull BedWars getBedWars();

    @NotNull MainConfig getMainConfig();

    @NotNull BedWarsGameUserRegistry getUserRegistry();

    @NotNull BedWarsGameMapManager getGameMapManager();

    @NotNull BedWarsGameVotingManager getGameVotingManager();

    Optional<BedWarsGameState> getGameState();

    void setGameState(@Nullable BedWarsGameState state);

    Optional<BedWarsGameMap> getGameMap();

    void setGameMap(@Nullable BedWarsGameMap gameMap);

    boolean isForceMapped();

    void forceMap(@NotNull BedWarsGameMap gameMap);
}
