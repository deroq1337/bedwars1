package com.github.deroq1337.bedwars.data.game.commands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.exceptions.EmptyGameStateException;
import com.github.deroq1337.bedwars.data.game.state.BedWarsGameState;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsForceMapCommand implements CommandExecutor {

    private final @NotNull BedWarsGame game;

    public BedWarsForceMapCommand(@NotNull BedWarsGame game) {
        this.game = game;
        Optional.ofNullable(game.getBedWars().getCommand("forcemap")).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
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
        if (!player.hasPermission("bedwars.forcemap")) {
            user.sendMessage("no_permission");
            return true;
        }

        if (args.length < 1) {
            user.sendMessage("command_forcemap_syntax");
            return true;
        }

        BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Error pausing game countdown: No GameState found"));
        if (!(gameState instanceof BedWarsLobbyState)) {
            user.sendMessage("game_already_started");
            return true;
        }

        if (game.isForceMapped()) {
            user.sendMessage("already_forcemapped");
            return true;
        }

        String mapName = args[0];
        if (game.getCurrentMap().isPresent() && game.getCurrentMap().get().getName().equals(mapName)) {
            user.sendMessage("already_forcemapped");
            return true;
        }

        game.getMapManager().getMapByName(mapName).thenAccept(map -> {
            Bukkit.getScheduler().runTask(game.getBedWars(), () -> {
                if (map.isEmpty()) {
                    user.sendMessage("command_map_not_found");
                    return;
                }

                game.forceMap(map.get());
                user.sendMessage("game_force_mapped");
            });
        });
        return true;
    }
}
