package com.github.deroq1337.bedwars.data.game.countdown;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVoting;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVotingCandidate;
import com.github.deroq1337.bedwars.data.game.voting.map.BedWarsGameMapVoting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("§aDas Spiel beginnt in §e" + getCurrent() + " Sekunden");

            if (tick == 10) {
                game.getGameVotingManager().determineWinners();
                game.getGameVotingManager().getVoting(BedWarsGameMapVoting.class, BedWarsGameMap.class)
                        .flatMap(BedWarsGameVoting::getWinner)
                        .ifPresent(winner -> game.setGameMap(winner.getValue()));
                announceVotingWinners(player);
            }
        });
    }

    private void announceVotingWinners(@NotNull Player player) {
        player.sendMessage("-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        game.getGameVotingManager().getVotingWinnerMap().forEach((votingClass, winner) -> {
            game.getGameVotingManager().getVoting((Class<? extends BedWarsGameVoting<?, ? extends BedWarsGameVotingCandidate<?>>>) votingClass).ifPresent(voting -> {
                player.sendMessage(voting.getName() + ": " + winner.getDisplayTitle());
            });
        });
        player.sendMessage("-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }
}
