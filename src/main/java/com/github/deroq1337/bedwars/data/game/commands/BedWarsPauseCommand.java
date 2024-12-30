package com.github.deroq1337.bedwars.data.game.commands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.countdown.BedWarsGameCountdown;
import com.github.deroq1337.bedwars.data.game.exceptions.EmptyGameStateException;
import com.github.deroq1337.bedwars.data.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsPauseCommand implements CommandExecutor {

    private final @NotNull BedWarsGame game;

    public BedWarsPauseCommand(@NotNull BedWarsGame game) {
        this.game = game;
        Optional.ofNullable(game.getBedWars().getCommand("pause")).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return true;
        }

        Optional<BedWarsGameUser> optionalUser = game.getUserRegistry().getUser(player.getUniqueId());
        if (optionalUser.isEmpty()) {
            player.sendMessage("§cAn error occurred. Rejoin or contact an administrator.");
            return true;
        }

        BedWarsGameUser user = optionalUser.get();
        if (!player.hasPermission("bedwars.pause")) {
            user.sendMessage("no_permission");
            return true;
        }

        BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Error pausing game countdown: No GameState found"));
        if (!(gameState instanceof BedWarsLobbyState)) {
            user.sendMessage("game_already_started");
            return true;
        }

        BedWarsGameCountdown countdown = gameState.getCountdown();
        if (countdown.isRunning()) {
            countdown.pause();
            user.sendMessage("countdown_paused");
        } else {
            countdown.unpause();
            user.sendMessage("countdown_resumed");
        }
        return true;
    }
}
