package com.github.deroq1337.bedwars.data.game.state;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.countdown.BedWarsInGameCountdown;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class BedWarsInGameState extends BedWarsGameState {

    public BedWarsInGameState(@NotNull BedWarsGame game) {
        super(game, new BedWarsInGameCountdown(game));
    }

    @Override
    public void enter() {
        game.getGameTeamManager().fillTeams();
        game.getUserRegistry().getAliveUsers().forEach(this::announceTeam);
    }

    @Override
    public void onJoin(@NotNull UUID uuid) {
        BedWarsGameUser user = game.getUserRegistry().addUser(uuid, false);
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

    private void announceTeam(@NotNull BedWarsGameUser user) {
        user.getTeam().ifPresent(team -> user.sendMessage("team_announcement", team.getNameWithColor(user)));
    }
}
