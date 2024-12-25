package com.github.deroq1337.bedwars.data.game.state;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.countdown.BedWarsLobbyCountdown;
import com.github.deroq1337.bedwars.data.game.item.Items;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsLobbyGameState extends BedWarsGameState {

    private static final int MIN_PLAYERS = 1;

    public BedWarsLobbyGameState(@NotNull BedWarsGame game) {
        super(game, new BedWarsLobbyCountdown(game));
    }

    @Override
    public void enter() {

    }

    @Override
    public void leave() {

    }

    @Override
    public void onJoin(@NotNull BedWarsUser user) {
        user.getBukkitPlayer().ifPresent(player -> {
            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setLevel(0);
            player.setExp(0);
            player.setFlying(false);
            player.setOp(false);
            player.setGameMode(GameMode.SURVIVAL);

            player.getInventory().setItem(0, Items.TEAM_SELECTOR_ITEM);
            player.getInventory().setItem(4, Items.VOTING_ITEM_ITEM);
            player.getInventory().setItem(7, Items.ACHIEVEMENT_ITEM);
            player.getInventory().setItem(8, Items.LOBBY_ITEM);
        });
    }

    @Override
    public boolean canStart() {
        return !getGame().getGameVotingManager().getGameMapVoting().getCandidates().isEmpty()
                && getGame().getUserRegistry().getUsers().size() == MIN_PLAYERS;
    }

    @Override
    public Optional<BedWarsGameState> getNextState() {
        return Optional.empty();
    }
}
