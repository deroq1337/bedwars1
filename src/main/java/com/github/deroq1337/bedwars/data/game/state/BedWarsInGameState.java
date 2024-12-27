package com.github.deroq1337.bedwars.data.game.state;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.countdown.BedWarsInGameCountdown;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class BedWarsInGameState extends BedWarsGameState {

    public BedWarsInGameState(@NotNull BedWarsGame game) {
        super(game, new BedWarsInGameCountdown(game));
    }

    @Override
    public void onJoin(@NotNull UUID uuid) {
        BedWarsUser user = game.getUserRegistry().addUser(uuid, false);
    }

    @Override
    public void enter() {

    }

    @Override
    public boolean canStart() {
        return !game.getUserRegistry().getAliveUsers().isEmpty();
    }

    @Override
    public Optional<BedWarsGameState> getNextState() {
        return Optional.empty();
    }
}
