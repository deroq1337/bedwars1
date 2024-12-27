package com.github.deroq1337.bedwars.data.game.countdown;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import org.jetbrains.annotations.NotNull;

public class BedWarsInGameCountdown extends BedWarsGameCountdown {

    public BedWarsInGameCountdown(@NotNull BedWarsGame game) {
        super(game, 60 * 60, 20, 30 * 60, 10 * 60, 5 * 60, 3 * 60, 60);
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onSpecialTick(int tick) {

    }
}
