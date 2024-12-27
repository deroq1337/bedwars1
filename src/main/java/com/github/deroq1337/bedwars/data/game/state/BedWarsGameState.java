package com.github.deroq1337.bedwars.data.game.state;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.countdown.BedWarsGameCountdown;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public abstract class BedWarsGameState {

    protected final @NotNull BedWarsGame game;
    private final @NotNull BedWarsGameCountdown countdown;

    public void enter() {
        game.setGameState(this);
    }

    public void leave()  {
        game.setGameState(null);
    }

    public abstract void onJoin(@NotNull UUID uuid);

    public abstract boolean canStart();

    public void check() {
        if (countdown.isStarted()) {
            if (!canStart()) {
                countdown.cancel();
                return;
            }
        }

        if (!countdown.isStarted()) {
            if (canStart()) {
                countdown.start();
            }
        }
    }

    public abstract Optional<BedWarsGameState> getNextState();
}
