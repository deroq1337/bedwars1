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

public class BedWarsMapAddSpawnerSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapAddSpawnerSubCommand(@NotNull BedWarsGame game) {
        super(game, "addSpawner");
    }

    @Override
    protected void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 2) {
            user.sendMessage("command_map_add_spawner_syntax");
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

        BedWarsGameMap gameMap = optionalGameMap.get();
        gameMap.addSpawnerLocation(resourceSpawnerType, new BedWarsDirectedGameMapLocation(player.getLocation()));

        if (!gameMapManager.saveMap(gameMap).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_spawner_added");
    }
}
