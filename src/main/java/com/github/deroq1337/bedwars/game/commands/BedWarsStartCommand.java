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

public class BedWarsStartCommand implements CommandExecutor {

    private final @NotNull BedWarsGame<?> game;

    public BedWarsStartCommand(@NotNull BedWarsGame game) {
        this.game = game;
        Optional.ofNullable(game.getBedWars().getCommand("start")).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("bedwars.start")) {
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
        if (countdown.getCurrent() <= 10) {
            player.sendMessage("§cDas Spiel startet bereits");
            return true;
        }

        countdown.setCurrent(10);
        if (!countdown.isRunning()) {
            countdown.setRunning(true);
        }

        player.sendMessage("§aDas Spiel wird gestartet");
        return true;
    }
}
