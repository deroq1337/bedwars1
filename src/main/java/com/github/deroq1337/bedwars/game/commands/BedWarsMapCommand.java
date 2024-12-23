package com.github.deroq1337.bedwars.game.commands;

import com.github.deroq1337.bedwars.game.BedWarsGame;
import com.github.deroq1337.bedwars.game.map.BedWarsGameMapManager;
import com.github.deroq1337.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.deroq1337.bedwars.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.game.teams.BedWarsGameTeamType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public class BedWarsMapCommand implements CommandExecutor {

    private final @NotNull BedWarsGameMapManager<DefaultBedWarsGameMap> gameMapManager;

    public BedWarsMapCommand(@NotNull BedWarsGame<DefaultBedWarsGameMap> game) {
        this.gameMapManager = game.getGameMapManager();
        Optional.ofNullable(game.getBedWars().getCommand("map")).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
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

        String name = args[1];
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

    private boolean createMap(@NotNull Player player, @NotNull String[] args) {
        String name = args[1];
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

        DefaultBedWarsGameMap gameMap = new DefaultBedWarsGameMap(name, teamCount, teamSize, minPlayers);
        if (!gameMapManager.createMap(gameMap).join()) {
            player.sendMessage("§cMap konnte nicht erstellt werden");
            return true;
        }

        player.sendMessage("§aMap wurde erstellt");
        return true;
    }

    private boolean handleMapOperation(@NotNull Player player, @NotNull String name, @NotNull Consumer<DefaultBedWarsGameMap> operation) {
        Optional<DefaultBedWarsGameMap> map = gameMapManager.getMapByName(name).join();
        if (map.isEmpty()) {
            player.sendMessage("§cEs gibt keine Map mit diesem Namen");
            return true;
        }

        operation.accept(map.get());
        return true;
    }

    private void deleteMap(@NotNull Player player, @NotNull DefaultBedWarsGameMap map) {
        if (!gameMapManager.deleteMap(map.getName()).join()) {
            player.sendMessage("§cMap konnte nicht gelöscht werden");
            return;
        }

        player.sendMessage("§aMap wurde gelöscht");
    }

    private void setLocation(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, @NotNull String operation) {
        switch (operation) {
            case "setspectator" -> {
                setSpectatorLocation(player, map);
                break;
            }
            case "setrespawn" -> {
                setRespawnLocation(player, map);
                break;
            }
        }
    }

    private void setSpectatorLocation(@NotNull Player player, @NotNull DefaultBedWarsGameMap map) {
        map.setSpectatorLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));
        gameMapManager.updateMap(map);
        player.sendMessage("§aSpectatorlocation wurde gesetzt");
    }

    private void setRespawnLocation(@NotNull Player player, @NotNull DefaultBedWarsGameMap map) {
        map.setRespawnLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));
        gameMapManager.updateMap(map);
        player.sendMessage("§aRespawnlocation wurde gesetzt");
    }

    private void renameMap(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, @NotNull String newName) {
        if (newName.equals(map.getName()) || gameMapManager.getMapByName(newName).join().isPresent()) {
            player.sendMessage("§cEs gibt bereits eine Map mit diesem Namen");
            return;
        }

        map.setName(newName);
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aMap-Name wurde geändert");
    }

    private void addShop(@NotNull Player player, @NotNull DefaultBedWarsGameMap map) {
        int id = map.addShopLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));
        gameMapManager.updateMap(map).join();
        player.sendMessage(String.format("§aShop #%s wurde hinzugefügt", id));
    }

    private void handleIntOperation(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, @NotNull String operation, @NotNull String value) {
        int intValue;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            player.sendMessage("§cGib eine valide Zahl an");
            return;
        }

        switch (operation) {
            case "setteamcount" -> {
                setTeamCount(player, map, intValue);
            }
            case "setteamsize" -> {
                setTeamSize(player, map, intValue);
            }
            case "setminplayers" -> {
                setMinPlayers(player, map, intValue);
            }
            case "removeshop" -> {
                removeShop(player, map, intValue);
            }
            default -> {
            }
        }
    }

    private void setTeamCount(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, int count) {
        map.setTeamCount(count);
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aTeamanzahl wurde gesetzt");
    }

    private void setTeamSize(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, int size) {
        map.setTeamSize(size);
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aTeamgröße wurde gesetzt");
    }

    private void setMinPlayers(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, int min) {
        map.setMinPlayers(min);
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aMindestanzahl an Spielern wurde gesetzt");
    }

    private void removeShop(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, int id) {
        if (!map.removeShopLocation(id)) {
            player.sendMessage("§cEs wurde kein Shop mit dieser ID gefunden");
            return;
        }

        gameMapManager.updateMap(map).join();
        player.sendMessage(String.format("§aShop wurde entfernt", id));
    }

    private void handleTeamOperation(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, @NotNull String operation, @NotNull String teamName) {
        BedWarsGameTeamType teamType;
        try {
            teamType = BedWarsGameTeamType.valueOf(teamName.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cUngültiges Team");
            return;
        }

        switch (operation) {
            case "addteam" -> {
                addTeam(player, map, teamType);
            }
            case "setteamspawn" -> {
                setTeamSpawn(player, map, teamType);
            }
            case "setteambed" -> {
                setTeamBed(player, map, teamType);
            }
            default -> {
                player.sendMessage("§cUnbekannte Operation");
            }
        }
    }

    private void addTeam(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, @NotNull BedWarsGameTeamType teamType) {
        if (!map.addTeam(teamType)) {
            player.sendMessage("§cDieses Team wurde bereits hinzugefügt");
            return;
        }

        gameMapManager.updateMap(map).join();
        player.sendMessage("§aTeam wurde hinzugefügt");
    }

    private void setTeamSpawn(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, @NotNull BedWarsGameTeamType teamType) {
        map.addTeamSpawnLocation(teamType, new BedWarsDirectedGameMapLocation(player.getLocation()));
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aTeamspawn wurde gesetzt");
    }

    private void setTeamBed(@NotNull Player player, @NotNull DefaultBedWarsGameMap map, @NotNull BedWarsGameTeamType teamType) {
        map.addTeamBedLocation(teamType, new BedWarsDirectedGameMapLocation(player.getLocation()));
        gameMapManager.updateMap(map).join();
        player.sendMessage("§aTeambett wurde gesetzt");
    }
}