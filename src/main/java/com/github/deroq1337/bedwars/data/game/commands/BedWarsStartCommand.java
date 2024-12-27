package com.github.deroq1337.bedwars.data.game.commands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.countdown.BedWarsGameCountdown;
import com.github.deroq1337.bedwars.data.game.exceptions.EmptyGameStateException;
import com.github.deroq1337.bedwars.data.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsStartCommand implements CommandExecutor {

    private final @NotNull BedWarsGame game;

    public BedWarsStartCommand(@NotNull BedWarsGame game) {
        this.game = game;
        Optional.ofNullable(game.getBedWars().getCommand("start")).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return true;
        }

        Optional<BedWarsUser> optionalUser = game.getUserRegistry().getUser(player.getUniqueId());
        if (optionalUser.isEmpty()) {
            player.sendMessage("§cAn error occurred. Rejoin or contact an administrator.");
            return true;
        }

        BedWarsUser user = optionalUser.get();
        if (!player.hasPermission("bedwars.start")) {
            user.sendMessage("no_permission");
            return true;
        }

        BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Error force starting game: No GameState found"));
        if (!(gameState instanceof BedWarsLobbyState)) {
            user.sendMessage("game_already_started");
            return true;
        }

        if (!gameState.canStart()) {
            user.sendMessage("not_enough_players");
            return true;
        }

        BedWarsGameCountdown countdown = gameState.getCountdown();
        if (countdown.getCurrent() <= 10) {
            user.sendMessage("game_already_starting");
            return true;
        }

        countdown.setCurrent(10);
        if (!countdown.isRunning()) {
            countdown.setRunning(true);
        }

        user.sendMessage("game_force_started");
        return true;
    }
}
