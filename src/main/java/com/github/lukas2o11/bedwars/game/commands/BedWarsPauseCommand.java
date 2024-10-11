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

public class BedWarsPauseCommand implements CommandExecutor {

    private final BedWarsGame game;

    public BedWarsPauseCommand(final BedWarsGame game) {
        this.game = game;
        game.getBedWars().getCommand("pause").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("bedwars.pause")) {
            player.sendMessage("§cKeine Rechte!");
            return true;
        }

        final BedWarsGameState gameState = game.getGameState()
                .orElseThrow(() -> new EmptyGameStateException("Error force-starting game: GameState in BedWarsGame is empty"));
        if (!(gameState instanceof BedWarsLobbyGameState)) {
            player.sendMessage("§cDas Spiel hat bereits begonnen");
            return true;
        }

        if (!gameState.canStart()) {
            player.sendMessage("§cEs sind zu wenige Spieler in der Lobby");
            return true;
        }

        final BedWarsCountdown countdown = gameState.getCountdown();

        if (countdown.isRunning()) {
            countdown.pause();
            player.sendMessage("§aDer Countdown wurde pausiert");
        } else {
            countdown.unpause();
            player.sendMessage("§aDer Countdown wird fortgesetzt");
        }
        return false;
    }
}
