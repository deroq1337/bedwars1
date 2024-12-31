package com.github.deroq1337.bedwars.data.game.scoreboard;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.scoreboard.models.BedWarsScoreboardScore;
import com.github.deroq1337.bedwars.data.game.team.BedWarsTeam;
import com.github.deroq1337.bedwars.data.game.team.BedWarsTeamType;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BedWarsInGameScoreboard extends BedWarsScoreboard {

    public BedWarsInGameScoreboard(@NotNull BedWarsGame game) {
        super(game);
    }

    @Override
    public void updateScoreboard(@NotNull BedWarsUser user) {
        user.getBukkitPlayer().ifPresent(player -> {
            Scoreboard scoreboard = player.getScoreboard();
            scoreboardScores.forEach(score -> getTeamByScore(score).ifPresent(team -> {
                updatePrefix(user, scoreboard, team);
                updateScore(scoreboard, score, team);
            }));
        });
    }

    @Override
    public @NotNull List<BedWarsScoreboardScore> getScoreboardScores() {
        return game.getTeamManager().getTeams().stream()
                .map(team -> new BedWarsScoreboardScore(team.getTeamType().name(), getScoreValue(team), false))
                .toList();
    }

    private Optional<BedWarsTeam> getTeamByScore(@NotNull BedWarsScoreboardScore score) {
        String teamName = score.getTeamName();

        try {
            BedWarsTeamType teamType = BedWarsTeamType.valueOf(score.getTeamName());
            return game.getTeamManager().getTeamByType(teamType);
        } catch (IllegalArgumentException e) {
            System.err.println("Scoreboard team '" + teamName + "' is no valid bedwars team");
            return Optional.empty();
        }
    }

    private void updatePrefix(@NotNull BedWarsUser user, @NotNull Scoreboard scoreboard, @NotNull BedWarsTeam team) {
        String prefix = user.getMessage(getScoreValue(team), team.getNameWithColor(user));
        Optional.ofNullable(scoreboard.getTeam(team.getTeamType().name())).ifPresent(sbTeam -> sbTeam.setPrefix(prefix));
    }

    private void updateScore(@NotNull Scoreboard scoreboard, @NotNull BedWarsScoreboardScore score, @NotNull BedWarsTeam team) {
        score.getEntry().ifPresent(entry -> {
            Optional.ofNullable(scoreboard.getObjective("bedwars")).ifPresent(objective -> objective.getScore(entry).setScore(team.getAliveUsers().size()));
        });
    }

    private @NotNull String getScoreValue(@NotNull BedWarsTeam team) {
        return "scoreboard_in_game_team_value_bed_" + (team.isBedDestroyed() ? "no" : "yes");
    }
}
