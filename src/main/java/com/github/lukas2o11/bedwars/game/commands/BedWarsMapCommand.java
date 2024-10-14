package com.github.lukas2o11.bedwars.game.commands;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.map.BedWarsGameMap;
import com.github.lukas2o11.bedwars.game.map.BedWarsGameMapManager;
import com.github.lukas2o11.bedwars.game.map.DefaultBedWarsGameMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsMapCommand implements CommandExecutor {

    private final BedWarsGameMapManager gameMapManager;

    public BedWarsMapCommand(BedWarsGame game) {
        this.gameMapManager = game.getGameMapManager();
        game.getBedWars().getCommand("map").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("bedwars.map")) {
            player.sendMessage("§cKeine Rechte!");
            return true;
        }

        if (args.length == 2)  {
            if (args[0].equalsIgnoreCase("delete")) {
                return deleteMap(player, args);
            }
        } else if (args.length == 3)  {
            if (args[0].equalsIgnoreCase("rename")) {
                return renameMap(player, args);
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("create")) {
                return createMap(player, args);
            }
        }
        return false;
    }

    private boolean createMap(final Player player, final String[] args) {
        final String name = args[1];
        if (gameMapManager.getMapByName(name).join().isPresent()) {
            player.sendMessage("§cEs gibt bereits eine Map mit diesem Namen");
            return true;
        }

        int teamCount;
        int teamSize;
        int minPlayers;
        try {
            teamCount = Integer.parseInt(args[2]);
            teamSize = Integer.parseInt(args[3]);
            minPlayers = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cGib eine valide Zahl an");
            return true;
        }

        final BedWarsGameMap gameMap = new DefaultBedWarsGameMap(name, teamCount, teamSize, minPlayers);
        if (!gameMapManager.createMap(gameMap).join()) {
            player.sendMessage("§cMap konnte nicht erstellt werden (insert wurde nicht anerkannt)");
            return true;
        }

        player.sendMessage("§aMap wurde erstellt");
        return true;
    }

    private boolean deleteMap(final Player player, final String[] args) {
        final String name = args[1];
        if (!gameMapManager.deleteMap(name).join()) {
            player.sendMessage("§cEs gibt keine Map mit diesem Namen");
            return true;
        }

        player.sendMessage("§aMap wurde gelöscht");
        return true;
    }

    private boolean renameMap(final Player player, final String[] args) {
        final String oldName = args[1];
        final Optional<BedWarsGameMap> oldFound = gameMapManager.getMapByName(oldName).join();
        if (oldFound.isEmpty()) {
            player.sendMessage("§cEs gibt keine Map mit diesem Namen");
            return true;
        }

        final BedWarsGameMap toUpdate = oldFound.get();
        final String newName = args[2];
        if (newName.equalsIgnoreCase(toUpdate.getName())
                || gameMapManager.getMapByName(newName).join().isPresent()) {
            player.sendMessage("§cEs gibt bereits eine Map mit diesem Namen");
            return true;
        }

        toUpdate.setName(newName);
        if (!gameMapManager.updateMap(toUpdate).join()) {
            player.sendMessage("§cMap konnte nicht aktualisiert werden");
            return true;
        }

        player.sendMessage("§aMap-Name wurde geändert");
        return true;
    }
}
