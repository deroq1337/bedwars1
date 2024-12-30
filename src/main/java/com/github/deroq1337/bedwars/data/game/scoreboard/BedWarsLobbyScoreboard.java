package com.github.deroq1337.bedwars.data.game.scoreboard;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.scoreboard.models.BedWarsGameScoreboardScore;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class BedWarsLobbyScoreboard extends BedWarsGameScoreboard {

    public BedWarsLobbyScoreboard(@NotNull BedWarsGame game) {
        super(game, Arrays.asList(
                new BedWarsGameScoreboardScore("scoreboard_lobby_map", "map", "scoreboard_lobby_map_value"),
                new BedWarsGameScoreboardScore("scoreboard_lobby_team_size", "teamSize", "scoreboard_lobby_team_size_value")
        ));
    }

    @Override
    public void updateScoreboard(@NotNull BedWarsGameUser user) {
        user.getBukkitPlayer().ifPresent(player -> {
            Scoreboard scoreboard = player.getScoreboard();
            Optional.ofNullable(scoreboard.getTeam("map")).ifPresent(team -> {
                String mapPrefix = game.getGameMap().map(map -> user.getMessage("scoreboard_lobby_map_value", map.getName()))
                        .orElseGet(() -> user.getMessage("scoreboard_lobby_map_value", user.getMessage("scoreboard_lobby_map_value_voting")));
                updateTeam(scoreboard, "map", mapPrefix);
            });

            String teamSizePrefix = user.getMessage("scoreboard_lobby_team_size_value",
                    game.getMainConfig().getTeamCount(), game.getMainConfig().getTeamSize());
            updateTeam(scoreboard, "teamSize", teamSizePrefix);
        });
    }

    private void updateTeam(@NotNull Scoreboard scoreboard, @NotNull String teamName, @NotNull String prefix) {
        Optional.ofNullable(scoreboard.getTeam(teamName)).ifPresent(team -> team.setPrefix(prefix));
    }
}
