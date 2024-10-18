package com.github.lukas2o11.bedwars.game.commands;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.map.BedWarsGameMapManager;
import com.github.lukas2o11.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.lukas2o11.bedwars.game.teams.BedWarsGameTeamType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public class BedWarsMapCommand implements CommandExecutor {

    private final BedWarsGameMapManager<DefaultBedWarsGameMap> gameMapManager;

    public BedWarsMapCommand(BedWarsGame game) {
        this.gameMapManager = game.getGameMapManager();
        game.getBedWars().getCommand("map").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("bedwars.map")) {
            player.sendMessage("§cKeine Rechte!");
            return true;
        }

        if (args.length < 2 || args.length > 5) {
            player.sendMessage("§cBefehl wurde nicht gefunden");
            return true;
        }

        final String name = args[1];

        if (args.length == 2) {
            switch (args[0].toLowerCase(Locale.ENGLISH)) {
                case "delete" -> {
                    return handleMapOperation(player, name, map -> deleteMap(player, map));
                }
                case "setspectator", "setrespawn" -> {
                    return handleMapOperation(player, name, map -> setLocation(player, map, args[0]));
                }
                case "addshop" -> {
                    return handleMapOperation(player, name, map -> addShop(player, map));
                }
                default -> {
                    player.sendMessage("§cBefehl wurde nicht gefunden");
                    return true;
                }
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase(Locale.ENGLISH)) {
                case "rename" -> {
                    return handleMapOperation(player, name, map -> renameMap(player, map, args[2]));
                }
                case "setteamcount", "setteamsize", "setminplayers", "removeshop" -> {
                    return handleMapOperation(player, name, map -> handleIntOperation(player, map, args[0], args[2]));
                }
                case "addteam", "setteamspawn", "setteambed" -> {
                    return handleMapOperation(player, name, map -> handleTeamOperation(player, map, args[0], args[2]));
                }
                case "create" -> {
                    return createMap(player, args);
                }
                default -> {
                    player.sendMessage("§cBefehl wurde nicht gefunden");
                    return true;
                }
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("create")) {
                return createMap(player, args);
            }

            player.sendMessage("§cBefehl wurde nicht gefunden");
            return true;
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

        final DefaultBedWarsGameMap gameMap = new DefaultBedWarsGameMap(name, teamCount, teamSize, minPlayers);
        if (!gameMapManager.createMap(gameMap).join()) {
            player.sendMessage("§cMap konnte nicht erstellt werden");
            return true;
        }

        player.sendMessage("§aMap wurde erstellt");
        return true;
    }

    private boolean handleMapOperation(final Player player, final String name, final Consumer<DefaultBedWarsGameMap> operation) {
        final Optional<DefaultBedWarsGameMap> map = gameMapManager.getMapByName(name).join();
        if (map.isEmpty()) {
            player.sendMessage("§cEs gibt keine Map mit diesem Namen");
            return true;
        }

        operation.accept(map.get());
        return true;
    }

    private boolean deleteMap(final Player player, final DefaultBedWarsGameMap map) {
        if (!gameMapManager.deleteMap(map.getName()).join()) {
            player.sendMessage("§cMap konnte nicht gelöscht werden");
            return true;
        }

        player.sendMessage("§aMap wurde gelöscht");
        return true;
    }

    private boolean setLocation(final Player player, final DefaultBedWarsGameMap map, final String operation) {
        switch (operation) {
            case "setspectator" -> {
                return setSpectatorLocation(player, map);
            }
            case "setrespawn" -> {
                return setRespawnLocation(player, map);
            }
            default -> {
                return true;
            }
        }
    }

    private boolean setSpectatorLocation(final Player player, final DefaultBedWarsGameMap map) {
        map.setSpectatorLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));
        gameMapManager.updateMap(map);
        player.sendMessage("§aSpectatorlocation wurde gesetzt");
        return true;
    }

    private boolean setRespawnLocation(final Player player, final DefaultBedWarsGameMap map) {
        map.setRespawnLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));
        gameMapManager.updateMap(map);
        player.sendMessage("§aRespawnlocation wurde gesetzt");
        return true;
    }

    private boolean renameMap(final Player player, final DefaultBedWarsGameMap map, final String newName) {
        if (newName.equals(map.getName()) || gameMapManager.getMapByName(newName).join().isPresent()) {
            player.sendMessage("§cEs gibt bereits eine Map mit diesem Namen");
            return true;
        }

        map.setName(newName);
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aMap-Name wurde geändert");
        return true;
    }

    private boolean addShop(final Player player, final DefaultBedWarsGameMap map) {
        final int id = map.addShopLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));
        gameMapManager.updateMap(map).join();
        player.sendMessage(String.format("§aShop #%s wurde hinzugefügt", id));
        return true;
    }

    private boolean handleIntOperation(final Player player, final DefaultBedWarsGameMap map, final String operation, final String value) {
        int intValue;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            player.sendMessage("§cGib eine valide Zahl an");
            return true;
        }

        switch (operation) {
            case "setteamcount" -> {
                return setTeamCount(player, map, intValue);
            }
            case "setteamsize" -> {
                return setTeamSize(player, map, intValue);
            }
            case "setminplayers" -> {
                return setMinPlayers(player, map, intValue);
            }
            case "removeshop" -> {
                return removeShop(player, map, intValue);
            }
            default -> {
                return true;
            }
        }
    }

    private boolean setTeamCount(final Player player, final DefaultBedWarsGameMap map, final int count) {
        map.setTeamCount(count);
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aTeamanzahl wurde gesetzt");
        return true;
    }

    private boolean setTeamSize(final Player player, final DefaultBedWarsGameMap map, final int size) {
        map.setTeamSize(size);
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aTeamgröße wurde gesetzt");
        return true;
    }

    private boolean setMinPlayers(final Player player, final DefaultBedWarsGameMap map, final int min) {
        map.setMinPlayers(min);
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aMindestanzahl an Spielern wurde gesetzt");
        return true;
    }

    private boolean removeShop(final Player player, final DefaultBedWarsGameMap map, final int id) {
        if (!map.removeShopLocation(id)) {
            player.sendMessage("§cEs wurde kein Shop mit dieser ID gefunden");
            return true;
        }

        gameMapManager.updateMap(map).join();
        player.sendMessage(String.format("§aShop wurde entfernt", id));
        return true;
    }

    private boolean handleTeamOperation(final Player player, final DefaultBedWarsGameMap map, final String operation, final String teamName) {
        BedWarsGameTeamType teamType;
        try {
            teamType = BedWarsGameTeamType.valueOf(teamName.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cUngültiges Team");
            return true;
        }

        switch (operation) {
            case "addteam" -> {
                return addTeam(player, map, teamType);
            }
            case "setteamspawn" -> {
                return setTeamSpawn(player, map, teamType);
            }
            case "setteambed" -> {
                return setTeamBed(player, map, teamType);
            }
            default -> {
                player.sendMessage("§cUnbekannte Operation");
                return true;
            }
        }
    }

    private boolean addTeam(Player player, DefaultBedWarsGameMap map, BedWarsGameTeamType teamType) {
        if (!map.addTeam(teamType)) {
            player.sendMessage("§cDieses Team wurde bereits hinzugefügt");
            return true;
        }

        gameMapManager.updateMap(map).join();
        player.sendMessage("§aTeam wurde hinzugefügt");
        return true;
    }

    private boolean setTeamSpawn(Player player, DefaultBedWarsGameMap map, BedWarsGameTeamType teamType) {
        map.addTeamSpawnLocation(teamType, new BedWarsDirectedGameMapLocation(player.getLocation()));
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aTeamspawn wurde gesetzt");
        return true;
    }

    private boolean setTeamBed(Player player, DefaultBedWarsGameMap map, BedWarsGameTeamType teamType) {
        map.addTeamBedLocation(teamType, new BedWarsDirectedGameMapLocation(player.getLocation()));
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aTeambett wurde gesetzt");
        return true;
    }
}