package com.github.deroq1337.bedwars.data.game.countdown;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVoting;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVotingCandidate;
import com.github.deroq1337.bedwars.data.game.voting.map.BedWarsGameMapVoting;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class BedWarsLobbyCountdown extends BedWarsGameCountdown {

    public BedWarsLobbyCountdown(@NotNull BedWarsGame game) {
        super(game, 60, 20, 60, 30, 10, 5, 4, 3, 2, 1);
    }

    @Override
    public void onTick() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setExp((float) getCurrent() / getStart());
            player.setLevel(getCurrent());
        });
    }

    @Override
    public void onSpecialTick(int tick) {
        game.getUserRegistry().getAliveUsers().forEach(user -> {
            if (tick == 10) {
                game.getGameVotingManager().determineWinners();
                announceVotingWinners(user);

                if (!game.isForceMapped()) {
                    game.getGameVotingManager().getVoting(BedWarsGameMapVoting.class, BedWarsGameMap.class)
                            .flatMap(BedWarsGameVoting::getWinner)
                            .ifPresent(winner -> game.setGameMap(winner.getValue()));
                }
            }

            if (tick == 1) {
                user.sendMessage("lobby_countdown_one");
            } else {
                user.sendMessage("lobby_countdown", tick);
            }
        });
    }

    private void announceVotingWinners(@NotNull BedWarsGameUser user) {
        game.getGameVotingManager().getVotingWinnerMap().forEach((votingClass, winner) -> {
            game.getGameVotingManager().getVoting((Class<? extends BedWarsGameVoting<?, ? extends BedWarsGameVotingCandidate<?>>>) votingClass).ifPresent(voting -> {
                user.sendMessage("lobby_countdown_voting_announcement", voting.getName(user), winner.getDisplayTitle(user), winner.getVotes().size());
            });
        });
    }
}
