package com.github.deroq1337.bedwars.data.game.scoreboard;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.scoreboard.models.BedWarsGameScoreboardScore;
import com.github.deroq1337.bedwars.data.game.team.BedWarsGameTeam;
import com.github.deroq1337.bedwars.data.game.team.BedWarsGameTeamType;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BedWarsInGameScoreboard extends BedWarsGameScoreboard {

    public BedWarsInGameScoreboard(@NotNull BedWarsGame game) {
        super(game);
    }

    @Override
    public void updateScoreboard(@NotNull BedWarsGameUser user) {
        user.getBukkitPlayer().ifPresent(player -> {
            Scoreboard scoreboard = player.getScoreboard();
            scoreboardScores.forEach(score -> getTeamByScore(score).ifPresent(team -> {
                updatePrefix(user, scoreboard, team);
                updateScore(scoreboard, score, team);
            }));
        });
    }

    @Override
    public @NotNull List<BedWarsGameScoreboardScore> getScoreboardScores() {
        return game.getGameTeamManager().getTeams().stream()
                .map(team -> new BedWarsGameScoreboardScore(team.getTeamType().name(), getScoreValue(team), false))
                .toList();
    }

    private Optional<BedWarsGameTeam> getTeamByScore(@NotNull BedWarsGameScoreboardScore score) {
        String teamName = score.getTeamName();

        try {
            BedWarsGameTeamType teamType = BedWarsGameTeamType.valueOf(score.getTeamName());
            return game.getGameTeamManager().getTeamByType(teamType);
        } catch (IllegalArgumentException e) {
            System.err.println("Scoreboard team '" + teamName + "' is no valid bedwars team");
            return Optional.empty();
        }
    }

    private void updatePrefix(@NotNull BedWarsGameUser user, @NotNull Scoreboard scoreboard, @NotNull BedWarsGameTeam team) {
        String prefix = user.getMessage(getScoreValue(team), team.getNameWithColor(user));
        Optional.ofNullable(scoreboard.getTeam(team.getTeamType().name())).ifPresent(sbTeam -> sbTeam.setPrefix(prefix));
    }

    private void updateScore(@NotNull Scoreboard scoreboard, @NotNull BedWarsGameScoreboardScore score, @NotNull BedWarsGameTeam team) {
        score.getEntry().ifPresent(entry -> {
            Optional.ofNullable(scoreboard.getObjective("bedwars")).ifPresent(objective -> objective.getScore(entry).setScore(team.getAliveUsers().size()));
        });
    }

    private @NotNull String getScoreValue(@NotNull BedWarsGameTeam team) {
        return "scoreboard_in_game_team_value_bed_" + (team.isBedDestroyed() ? "no" : "yes");
    }
}
