package com.github.deroq1337.bedwars.data.game.scoreboard;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.scoreboard.models.BedWarsScoreboardScore;
import com.github.deroq1337.bedwars.data.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BedWarsScoreboard {

    protected final @NotNull BedWarsGame game;
    private final @NotNull Class<? extends BedWarsGameState> gameState;
    protected final @NotNull List<BedWarsScoreboardScore> scoreboardScores;

    public BedWarsScoreboard(@NotNull BedWarsGame game, @NotNull Class<? extends BedWarsGameState> gameState) {
        this.game = game;
        this.gameState = gameState;
        this.scoreboardScores = getScoreboardScores();
    }

    public void setScoreboard(@NotNull BedWarsUser user) {
        Optional.ofNullable(Bukkit.getScoreboardManager()).ifPresent(scoreboardManager -> {
            Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("bedwars", Criteria.DUMMY, "§lGommeHD Test");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            AtomicInteger scoreIndex = new AtomicInteger(scoreboardScores.size() * 2 + (scoreboardScores.size() - 1));
            // empty score as first line
            setEmptyScore(objective, scoreIndex.get());
            scoreIndex.getAndDecrement();

            for (BedWarsScoreboardScore scoreboardScore : scoreboardScores) {
                scoreboardScore.getName().ifPresent(scoreName -> {
                    Score score = objective.getScore(user.getMessage(scoreName));
                    score.setScore(scoreIndex.get());
                    scoreIndex.set(scoreIndex.get() - 1);
                });

                Team team = getTeam(scoreboard, scoreboardScore);
                String entry = generateRandomEntry(scoreboard);
                team.addEntry(entry);
                team.setPrefix(user.getMessage(scoreboardScore.getValue()));
                objective.getScore(entry).setScore(scoreIndex.get());
                scoreboardScore.setEntry(Optional.of(entry));
                scoreIndex.getAndDecrement();

                if (scoreboardScore.isFreeSpace()) {
                    setEmptyScore(objective, scoreIndex.get());
                    scoreIndex.getAndDecrement();
                }
            }

            user.getBukkitPlayer().ifPresent(player -> player.setScoreboard(scoreboard));
            startUpdateScoreboardTask(user);
        });
    }

    public abstract void updateScoreboard(@NotNull BedWarsUser user);

    public abstract @NotNull List<BedWarsScoreboardScore> getScoreboardScores();

    private void startUpdateScoreboardTask(@NotNull BedWarsUser user) {
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean cancel = game.getGameState()
                        .map(currentState -> !currentState.getClass().equals(game.getGameState().get().getClass()))
                        .orElse(true);
                if (cancel) {
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

    private @NotNull Team getTeam(@NotNull Scoreboard scoreboard, @NotNull BedWarsScoreboardScore scoreboardScore) {
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
