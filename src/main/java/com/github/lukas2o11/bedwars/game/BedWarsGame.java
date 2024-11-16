package com.github.lukas2o11.bedwars.game;

import com.github.lukas2o11.bedwars.BedWars;
import com.github.lukas2o11.bedwars.game.map.BedWarsGameMap;
import com.github.lukas2o11.bedwars.game.map.BedWarsGameMapManager;
import com.github.lukas2o11.bedwars.game.state.BedWarsGameState;
import com.github.lukas2o11.bedwars.game.user.BedWarsUserRegistry;
import com.github.lukas2o11.bedwars.game.voting.BedWarsGameVotingManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface BedWarsGame<M extends BedWarsGameMap> {

    @NotNull BedWars getBedWars();

    @NotNull BedWarsUserRegistry getUserRegistry();

    @NotNull BedWarsGameMapManager<M> getGameMapManager();

    @NotNull BedWarsGameVotingManager getGameVotingManager();

    Optional<BedWarsGameState> getGameState();

    void setGameState(@Nullable BedWarsGameState state);

    Optional<M> getGameMap();
}
