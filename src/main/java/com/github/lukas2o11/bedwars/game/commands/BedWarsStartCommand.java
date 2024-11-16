package com.github.lukas2o11.bedwars.game.commands;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.countdown.BedWarsCountdown;
import com.github.lukas2o11.bedwars.game.exceptions.EmptyGameStateException;
import com.github.lukas2o11.bedwars.game.state.BedWarsGameState;
import com.github.lukas2o11.bedwars.game.state.BedWarsLobbyGameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BedWarsStartCommand implements CommandExecutor {

    private final BedWarsGame<?> game;

    public BedWarsStartCommand(@NotNull BedWarsGame game) {
        this.game = game;
        game.getBedWars().getCommand("start").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("bedwars.start")) {
            player.sendMessage("§cKeine Rechte!");
            return true;
        }

        final BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Error force-starting game: GameState in BedWarsGame is empty"));
        if (!(gameState instanceof BedWarsLobbyGameState)) {
            player.sendMessage("§cDas Spiel hat bereits begonnen");
            return true;
        }

        if (!gameState.canStart()) {
            player.sendMessage("§cEs sind zu wenige Spieler in der Lobby");
            return true;
        }

        final BedWarsCountdown countdown = gameState.getCountdown();
        if (countdown.getCurrent() <= 10) {
            player.sendMessage("§cDas Spiel startet bereits");
            return true;
        }

        countdown.setCurrent(10);
        if (!countdown.isRunning()) {
            countdown.setRunning(true);
        }

        player.sendMessage("§aDas Spiel wird gestartet");
        return false;
    }
}
