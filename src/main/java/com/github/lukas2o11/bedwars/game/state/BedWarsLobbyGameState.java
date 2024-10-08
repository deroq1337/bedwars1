package com.github.lukas2o11.bedwars.game.state;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.countdown.BedWarsLobbyCountdown;

import java.util.Optional;

public class BedWarsLobbyGameState extends BedWarsGameState {

    public BedWarsLobbyGameState(final BedWarsGame game) {
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
        return getGame().getUserRegistry().listUsers().size() >= BedWarsGame.STATE_LOBBY_MIN_PLAYERS;
    }

    @Override
    public Optional<BedWarsGameState> getNextState() {
        return Optional.empty();
    }
}
