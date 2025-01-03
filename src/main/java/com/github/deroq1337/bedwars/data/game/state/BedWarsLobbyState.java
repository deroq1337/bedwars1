package com.github.deroq1337.bedwars.data.game.state;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.countdown.BedWarsLobbyCountdown;
import com.github.deroq1337.bedwars.data.game.scoreboard.BedWarsLobbyScoreboard;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import com.github.deroq1337.bedwars.data.game.voting.map.BedWarsGameMapVoting;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class BedWarsLobbyState extends BedWarsGameState {

    private final int minPlayers;

    public BedWarsLobbyState(@NotNull BedWarsGame game) {
        super(game, new BedWarsLobbyCountdown(game));
        this.minPlayers = game.getMainConfig().getMinPlayers();
    }

    @Override
    public void enter() {

    }

    @Override
    public void leave() {

    }

    @Override
    public boolean check() {
        boolean check = super.check();
        if (!check) {
            game.getVotingManager().resetWinners();
            if (!game.isForceMapped()) {
                game.setCurrentMap(null);
            }
        }
        return check;
    }

    @Override
    public void onJoin(@NotNull UUID uuid) {
        BedWarsUser user = game.getUserRegistry().addUser(uuid, true);
        user.getBukkitPlayer().ifPresent(player -> {
            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setLevel(0);
            player.setExp(0);
            player.setFlying(false);
            player.setOp(false);
            player.setGameMode(GameMode.SURVIVAL);

            player.getInventory().setItem(0, game.getTeamManager().getItem(user));
            player.getInventory().setItem(4, game.getVotingManager().getItem(user));

            new BedWarsLobbyScoreboard(game).setScoreboard(user);
        });
    }

    @Override
    public void onQuit(@NotNull UUID uuid) {
        game.getUserRegistry().getUser(uuid).ifPresent(user -> {
            game.getVotingManager().clearVotes(user.getUuid());
            game.getUserRegistry().removeUser(uuid);
        });
    }

    @Override
    public boolean canStart() {
        return game.getVotingManager().getVoting(BedWarsGameMapVoting.class)
                .map(voting -> !voting.getCandidates().isEmpty())
                .orElse(false) && getGame().getUserRegistry().getUsers().size() >= minPlayers;
    }

    @Override
    public Optional<BedWarsGameState> getNextState() {
        return Optional.of(new BedWarsInGameState(game));
    }
}
