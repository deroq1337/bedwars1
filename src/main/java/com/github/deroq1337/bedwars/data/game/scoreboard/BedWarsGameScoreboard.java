package com.github.deroq1337.bedwars.data.game.scoreboard;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.scoreboard.models.BedWarsGameScoreboardScore;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BedWarsGameScoreboard {

    protected final @NotNull BedWarsGame game;
    protected final @NotNull List<BedWarsGameScoreboardScore> scoreboardScores;

    public BedWarsGameScoreboard(@NotNull BedWarsGame game, @NotNull List<BedWarsGameScoreboardScore> scoreboardScores) {
        this.game = game;
        this.scoreboardScores = scoreboardScores;
    }

    public void setScoreboard(@NotNull BedWarsGameUser user) {
        Optional.ofNullable(Bukkit.getScoreboardManager()).ifPresent(scoreboardManager -> {
            Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("serverName", Criteria.DUMMY, "§lGommeHD Test");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            int scoreIndex = scoreboardScores.size() * 2 + (scoreboardScores.size() - 1);
            for (BedWarsGameScoreboardScore scoreboardScore : scoreboardScores) {
                setEmptyScore(objective, scoreIndex);
                scoreIndex--;

                Score score = objective.getScore(user.getMessage(scoreboardScore.getName()));
                score.setScore(scoreIndex);
                scoreIndex--;

                Team team = getTeam(scoreboard, scoreboardScore);
                String entry = generateRandomEntry(scoreboard);
                team.addEntry(entry);
                team.setPrefix(user.getMessage(scoreboardScore.getValue()));
                objective.getScore(entry).setScore(scoreIndex);
                scoreIndex--;
            }

            user.getBukkitPlayer().ifPresent(player -> player.setScoreboard(scoreboard));
            startUpdateScoreboardTask(user);
        });
    }

    public abstract void updateScoreboard(@NotNull BedWarsGameUser user);

    private void startUpdateScoreboardTask(@NotNull BedWarsGameUser user) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (game.getGameState().isEmpty() || !(game.getGameState().get() instanceof BedWarsLobbyState) || user.getBukkitPlayer().isEmpty()) {
                    cancel();
                    return;
                }

                updateScoreboard(user);
            }
        }.runTaskTimer(game.getBedWars(), 0, 20L);
    }

    private void setEmptyScore(@NotNull Objective objective, int scoreIndex) {
        Score score = objective.getScore(" ".repeat(Math.max(0, scoreIndex)));
        score.setScore(scoreIndex);
    }

    private @NotNull Team getTeam(@NotNull Scoreboard scoreboard, @NotNull BedWarsGameScoreboardScore scoreboardScore) {
        return Optional.ofNullable(scoreboard.getTeam(scoreboardScore.getTeamName()))
                .orElseGet(() -> scoreboard.registerNewTeam(scoreboardScore.getTeamName()));
    }

    private @NotNull String generateRandomEntry(@NotNull Scoreboard scoreboard) {
        ChatColor[] values = ChatColor.values();
        for (int tries = 100; tries > 0; tries--) {
            String entry = values[ThreadLocalRandom.current().nextInt(values.length)] + "" +
                    values[ThreadLocalRandom.current().nextInt(values.length)] + "" +
                    values[ThreadLocalRandom.current().nextInt(values.length)];
            if (scoreboard.getEntryTeam(entry) == null) {
                return entry;
            }
        }

        System.err.println("All generated random entries were existing");
        return ChatColor.BLACK + "" + ChatColor.WHITE;
    }
}
