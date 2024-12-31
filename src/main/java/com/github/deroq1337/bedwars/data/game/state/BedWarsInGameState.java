package com.github.deroq1337.bedwars.data.game.state;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.countdown.BedWarsInGameCountdown;
import com.github.deroq1337.bedwars.data.game.scoreboard.BedWarsInGameScoreboard;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class BedWarsInGameState extends BedWarsGameState {

    public BedWarsInGameState(@NotNull BedWarsGame game) {
        super(game, new BedWarsInGameCountdown(game));
    }

    @Override
    public void enter() {
        game.getTeamManager().fillTeams();
        game.getTeamManager().initLocations();
        game.getTeamManager().destroyBeds(game.getTeamManager().getTeams());
        game.getUserRegistry().getAliveUsers().forEach(user -> {
            user.getTeam().ifPresent(team -> {
                team.teleport(user);
                team.announce(user);
            });

            new BedWarsInGameScoreboard(game).setScoreboard(user);
        });
    }

    @Override
    public void onJoin(@NotNull UUID uuid) {
        BedWarsUser user = game.getUserRegistry().addUser(uuid, false);
    }

    @Override
    public void onQuit(@NotNull UUID uuid) {

    }

    @Override
    public boolean canStart() {
        return !game.getUserRegistry().getAliveUsers().isEmpty();
    }

    @Override
    public Optional<BedWarsGameState> getNextState() {
        return Optional.empty();
    }
}
