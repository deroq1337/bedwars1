package com.github.deroq1337.bedwars.game.commands;

import com.github.deroq1337.bedwars.game.BedWarsGame;
import com.github.deroq1337.bedwars.game.countdown.BedWarsCountdown;
import com.github.deroq1337.bedwars.game.exceptions.EmptyGameStateException;
import com.github.deroq1337.bedwars.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.game.state.BedWarsLobbyGameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsPauseCommand implements CommandExecutor {

    private @NotNull final BedWarsGame<?> game;

    public BedWarsPauseCommand(@NotNull BedWarsGame game) {
        this.game = game;
        Optional.ofNullable(game.getBedWars().getCommand("pause")).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("bedwars.pause")) {
            player.sendMessage("§cKeine Rechte!");
            return true;
        }

        BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Error force-starting game: GameState in BedWarsGame is empty"));
        if (!(gameState instanceof BedWarsLobbyGameState)) {
            player.sendMessage("§cDas Spiel hat bereits begonnen");
            return true;
        }

        if (!gameState.canStart()) {
            player.sendMessage("§cEs sind zu wenige Spieler in der Lobby");
            return true;
        }

        BedWarsCountdown countdown = gameState.getCountdown();
        if (countdown.isRunning()) {
            countdown.pause();
            player.sendMessage("§aDer Countdown wurde pausiert");
        } else {
            countdown.unpause();
            player.sendMessage("§aDer Countdown wird fortgesetzt");
        }
        return true;
    }
}
