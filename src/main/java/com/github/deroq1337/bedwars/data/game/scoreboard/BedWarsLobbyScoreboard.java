package com.github.deroq1337.bedwars.data.game.scoreboard;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.scoreboard.models.BedWarsScoreboardScore;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BedWarsLobbyScoreboard extends BedWarsScoreboard {

    public BedWarsLobbyScoreboard(@NotNull BedWarsGame game) {
        super(game, BedWarsLobbyState.class);
    }

    @Override
    public void updateScoreboard(@NotNull BedWarsUser user) {
        user.getBukkitPlayer().ifPresent(player -> {
            Scoreboard scoreboard = player.getScoreboard();
            String mapPrefix = game.getCurrentMap()
                    .map(map -> user.getMessage("scoreboard_lobby_map_value", map.getName()))
                    .orElseGet(() -> user.getMessage("scoreboard_lobby_map_value", user.getMessage("scoreboard_lobby_map_value_voting")));
            updatePrefix(scoreboard, "map", mapPrefix);

            String teamSizePrefix = user.getMessage("scoreboard_lobby_team_size_value",
                    game.getMainConfig().getTeamCount(), game.getMainConfig().getTeamSize());
            updatePrefix(scoreboard, "teamSize", teamSizePrefix);
        });
    }

    @Override
    public @NotNull List<BedWarsScoreboardScore> getScoreboardScores() {
        return List.of(
                new BedWarsScoreboardScore(Optional.of("scoreboard_lobby_map"), "map", "scoreboard_lobby_map_value", true),
                new BedWarsScoreboardScore(Optional.of("scoreboard_lobby_team_size"), "teamSize", "scoreboard_lobby_team_size_value", false)
        );
    }

    private void updatePrefix(@NotNull Scoreboard scoreboard, @NotNull String teamName, @NotNull String prefix) {
        Optional.ofNullable(scoreboard.getTeam(teamName)).ifPresent(team -> team.setPrefix(prefix));
    }
}
