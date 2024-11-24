package com.github.lukas2o11.bedwars.game.state;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.countdown.BedWarsLobbyCountdown;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsLobbyGameState extends BedWarsGameState {

    public BedWarsLobbyGameState(@NotNull BedWarsGame game) {
        super(game, new BedWarsLobbyCountdown(game));
    }

    @Override
    public void enter() {

    }

    @Override
    public void leave() {

    }

    @Override
    public boolean canStart() {
        return getGame().getGameVotingManager().getGameMapVoting().getCandidates().size() > 0
                && getGame().getUserRegistry().getUsers().size() >= 1;
    }

    @Override
    public Optional<BedWarsGameState> getNextState() {
        return Optional.empty();
    }
}
