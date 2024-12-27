package com.github.deroq1337.bedwars.data.game.commands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMapManager;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.data.game.teams.BedWarsGameTeamType;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public class BedWarsMapCommand implements CommandExecutor {

    private final @NotNull BedWarsGame game;
    private final @NotNull BedWarsGameMapManager gameMapManager;

    public BedWarsMapCommand(@NotNull BedWarsGame game) {
        this.game = game;
        this.gameMapManager = game.getGameMapManager();
        Optional.ofNullable(game.getBedWars().getCommand("map")).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        Optional<BedWarsUser> optionalUser = game.getUserRegistry().getUser(player.getUniqueId());
        if (optionalUser.isEmpty()) {
            player.sendMessage("§cAn error occurred. Rejoin or contact an administrator.");
            return true;
        }

        BedWarsUser user = optionalUser.get();
        if (!player.hasPermission("bedwars.map")) {
            user.sendMessage("no_permission");
            return true;
        }

        if (args.length < 2 || args.length > 5) {
            player.sendMessage("command_not_found");
            return true;
        }

        String name = args[1];
        if (args.length == 2) {
            switch (args[0].toLowerCase(Locale.ENGLISH)) {
                case "delete" -> {
                    return handleMapOperation(user, name, map -> deleteMap(user, map));
                }
                case "setspectator", "setrespawn" -> {
                    return handleMapOperation(user, name, map -> setLocation(user, map, args[0]));
                }
                case "addshop" -> {
                    return handleMapOperation(user, name, map -> addShop(user, map));
                }
                default -> {
                    user.sendMessage("command_not_found");
                    return true;
                }
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase(Locale.ENGLISH)) {
                case "rename" -> {
                    return handleMapOperation(user, name, map -> renameMap(user, map, args[2]));
                }
                case "setteamcount", "setteamsize", "setminplayers", "removeshop" -> {
                    return handleMapOperation(user, name, map -> handleIntOperation(user, map, args[0], args[2]));
                }
                case "addteam", "setteamspawn", "setteambed" -> {
                    return handleMapOperation(user, name, map -> handleTeamOperation(user, map, args[0], args[2]));
                }
                case "create" -> {
                    return createMap(user, args);
                }
                default -> {
                    user.sendMessage("command_not_found");
                    return true;
                }
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("create")) {
                return createMap(user, args);
            }

            player.sendMessage("command_not_found");
            return true;
        }

        return false;
    }

    private boolean createMap(@NotNull BedWarsUser user, @NotNull String[] args) {
        String name = args[1];
        if (gameMapManager.getMapByName(name).join().isPresent()) {
            user.sendMessage("map_already_exists");
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
            user.sendMessage("invalid_number");
            return true;
        }

        BedWarsGameMap gameMap = new BedWarsGameMap(name, teamCount, teamSize, minPlayers);
        if (!gameMapManager.createMap(gameMap).join()) {
            user.sendMessage("map_not_created");
            return true;
        }

        user.sendMessage("map_created");
        return true;
    }

    private boolean handleMapOperation(@NotNull BedWarsUser user, @NotNull String name, @NotNull Consumer<BedWarsGameMap> operation) {
        Optional<BedWarsGameMap> map = gameMapManager.getMapByName(name).join();
        if (map.isEmpty()) {
            user.sendMessage("map_not_found");
            return true;
        }

        operation.accept(map.get());
        return true;
    }

    private void deleteMap(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map) {
        if (!gameMapManager.deleteMap(map.getName()).join()) {
            user.sendMessage("map_not_deleted");
            return;
        }

        user.sendMessage("map_deleted");
    }

    private void setLocation(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, @NotNull String operation) {
        switch (operation) {
            case "setspectator" -> {
                setSpectatorLocation(user, map);
                break;
            }
            case "setrespawn" -> {
                setRespawnLocation(user, map);
                break;
            }
        }
    }

    private void setSpectatorLocation(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map) {
        user.getBukkitPlayer().ifPresent(player -> {
            map.setSpectatorLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));
            gameMapManager.updateMap(map);
            user.sendMessage("map_spectator_set");
        });
    }

    private void setRespawnLocation(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map) {
        user.getBukkitPlayer().ifPresent(player -> {
            map.setRespawnLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));
            gameMapManager.updateMap(map);
            user.sendMessage("map_respawn_set");
        });
    }

    private void renameMap(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, @NotNull String newName) {
        if (newName.equals(map.getName()) || gameMapManager.getMapByName(newName).join().isPresent()) {
            user.sendMessage("map_already_exists");
            return;
        }

        map.setName(newName);
        gameMapManager.updateMap(map).join();
        user.sendMessage("map_name_changed");
    }

    private void addShop(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map) {
        user.getBukkitPlayer().ifPresent(player -> {
            int id = map.addShopLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));
            gameMapManager.updateMap(map).join();
            user.sendMessage("map_shop_added", id);
        });
    }

    private void handleIntOperation(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, @NotNull String operation, @NotNull String value) {
        int intValue;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            user.sendMessage("invalid_number");
            return;
        }

        switch (operation) {
            case "setteamcount" -> {
                setTeamCount(user, map, intValue);
            }
            case "setteamsize" -> {
                setTeamSize(user, map, intValue);
            }
            case "setminplayers" -> {
                setMinPlayers(user, map, intValue);
            }
            case "removeshop" -> {
                removeShop(user, map, intValue);
            }
            default -> {
            }
        }
    }

    private void setTeamCount(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, int count) {
        map.setTeamCount(count);
        gameMapManager.updateMap(map).join();
        user.sendMessage("map_team_count_set");
    }

    private void setTeamSize(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, int size) {
        map.setTeamSize(size);
        gameMapManager.updateMap(map).join();
        user.sendMessage("map_team_size_set");
    }

    private void setMinPlayers(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, int min) {
        map.setMinPlayers(min);
        gameMapManager.updateMap(map).join();
        user.sendMessage("map_min_players_set");
    }

    private void removeShop(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, int id) {
        if (!map.removeShopLocation(id)) {
            user.sendMessage("map_shop_not_found");
            return;
        }

        gameMapManager.updateMap(map).join();
        user.sendMessage("map_shop_removed");
    }

    private void handleTeamOperation(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, @NotNull String operation, @NotNull String teamName) {
        BedWarsGameTeamType teamType;
        try {
            teamType = BedWarsGameTeamType.valueOf(teamName.toUpperCase());
        } catch (IllegalArgumentException e) {
            user.sendMessage("map_invalid_team");
            return;
        }

        switch (operation) {
            case "addteam" -> {
                addTeam(user, map, teamType);
            }
            case "setteamspawn" -> {
                setTeamSpawn(user, map, teamType);
            }
            case "setteambed" -> {
                setTeamBed(user, map, teamType);
            }
        }
    }

    private void addTeam(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, @NotNull BedWarsGameTeamType teamType) {
        if (!map.addTeam(teamType)) {
            user.sendMessage("map_team_already_added");
            return;
        }

        gameMapManager.updateMap(map).join();
        user.sendMessage("map_team_added");
    }

    private void setTeamSpawn(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, @NotNull BedWarsGameTeamType teamType) {
        user.getBukkitPlayer().ifPresent(player -> {
            map.addTeamSpawnLocation(teamType, new BedWarsDirectedGameMapLocation(player.getLocation()));
            gameMapManager.updateMap(map).join();
            user.sendMessage("map_team_spawn_set");
        });
    }

    private void setTeamBed(@NotNull BedWarsUser user, @NotNull BedWarsGameMap map, @NotNull BedWarsGameTeamType teamType) {
        user.getBukkitPlayer().ifPresent(player -> {
            map.addTeamBedLocation(teamType, new BedWarsDirectedGameMapLocation(player.getLocation()));
            gameMapManager.updateMap(map).join();
            user.sendMessage("map_team_bed_set");
        });
    }
}