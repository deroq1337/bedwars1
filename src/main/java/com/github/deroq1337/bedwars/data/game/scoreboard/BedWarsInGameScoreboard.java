package com.github.deroq1337.bedwars.data.game.scoreboard;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.scoreboard.models.BedWarsGameScoreboardScore;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BedWarsInGameScoreboard extends BedWarsGameScoreboard {

    public BedWarsInGameScoreboard(@NotNull BedWarsGame game, @NotNull List<BedWarsGameScoreboardScore> scoreboardScores) {
        super(game, scoreboardScores);
    }

    @Override
    public void updateScoreboard(@NotNull BedWarsGameUser user) {

    }
}
