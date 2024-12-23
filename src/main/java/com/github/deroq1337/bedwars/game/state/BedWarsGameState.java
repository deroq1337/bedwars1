package com.github.deroq1337.bedwars.game.state;

import com.github.deroq1337.bedwars.game.BedWarsGame;
import com.github.deroq1337.bedwars.game.countdown.BedWarsCountdown;
import com.github.deroq1337.bedwars.game.user.BedWarsUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
public abstract class BedWarsGameState {

    private @NotNull final BedWarsGame game;
    private @NotNull final BedWarsCountdown countdown;

    public void enter() {
        game.setGameState(this);
    }

    public void leave()  {
        game.setGameState(null);
    }

    public abstract boolean canStart();

    public abstract void onJoin(@NotNull BedWarsUser user);

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
