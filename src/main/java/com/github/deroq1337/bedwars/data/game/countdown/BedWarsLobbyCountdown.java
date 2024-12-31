package com.github.deroq1337.bedwars.data.game.countdown;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMap;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsVoting;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsVotingCandidate;
import com.github.deroq1337.bedwars.data.game.voting.map.BedWarsGameMapVoting;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public class BedWarsLobbyCountdown extends BedWarsCountdown {

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
                game.getVotingManager().determineWinners();
                announceVotingWinners(user);

                if (!game.isForceMapped()) {
                    game.getVotingManager().getVoting(BedWarsGameMapVoting.class, BedWarsMap.class)
                            .flatMap(BedWarsVoting::getWinner)
                            .ifPresent(winner -> game.setCurrentMap(winner.getValue()));
                }
            }

            String message = "lobby_countdown" + (tick == 1 ? "_one" : "");
            user.sendMessage(message, tick);
            user.getBukkitPlayer().ifPresent(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f));
        });
    }

    private void announceVotingWinners(@NotNull BedWarsUser user) {
        game.getVotingManager().getVotingWinnerMap().forEach((votingClass, winner) -> {
            game.getVotingManager().getVoting((Class<? extends BedWarsVoting<?, ? extends BedWarsVotingCandidate<?>>>) votingClass).ifPresent(voting -> {
                user.sendMessage("lobby_countdown_voting_announcement", voting.getName(user), winner.getDisplayTitle(user), winner.getVotes().size());
            });
        });
    }
}
