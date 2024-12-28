package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.data.game.spawners.BedWarsGameResourceSpawnerType;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class BedWarsMapRemoveSpawnerSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapRemoveSpawnerSubCommand(@NotNull BedWarsGame game) {
        super(game, "removeSpawner");
    }

    @Override
    protected void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 3) {
            user.sendMessage("command_map_remove_spawner_syntax");
            return;
        }

        String mapName = args[0];
        Optional<BedWarsGameMap> optionalGameMap = gameMapManager.getMapByName(mapName).join();
        if (optionalGameMap.isEmpty()) {
            user.sendMessage("command_map_not_found");
            return;
        }

        String resourceName = args[1].toUpperCase();
        BedWarsGameResourceSpawnerType resourceSpawnerType;
        try {
            resourceSpawnerType = BedWarsGameResourceSpawnerType.valueOf(resourceName);
        } catch (IllegalArgumentException e) {
            user.sendMessage("command_map_invalid_resource_type", Arrays.toString(BedWarsGameResourceSpawnerType.values()));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            user.sendMessage("invalid_number");
            return;
        }

        BedWarsGameMap gameMap = optionalGameMap.get();
        if (!gameMap.removeSpawnerLocation(resourceSpawnerType, id)) {
            user.sendMessage("command_map_spawner_not_found");
            return;
        }

        if (!gameMapManager.updateMap(gameMap).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_spawner_removed");
    }
}
