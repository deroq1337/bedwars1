package com.github.deroq1337.bedwars.game;

import com.github.deroq1337.bedwars.BedWars;
import com.github.deroq1337.bedwars.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.game.map.BedWarsGameMapManager;
import com.github.deroq1337.bedwars.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.game.user.BedWarsUserRegistry;
import com.github.deroq1337.bedwars.game.voting.BedWarsGameVotingManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface BedWarsGame<M extends BedWarsGameMap> {

    @NotNull BedWars getBedWars();

    @NotNull BedWarsUserRegistry getUserRegistry();

    @NotNull BedWarsGameMapManager<M> getGameMapManager();

    @NotNull BedWarsGameVotingManager<M> getGameVotingManager();

    Optional<BedWarsGameState> getGameState();

    void setGameState(@Nullable BedWarsGameState state);

    Optional<M> getGameMap();
}
