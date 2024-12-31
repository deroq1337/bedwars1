package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMap;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsMapDirectedLocation;
import com.github.deroq1337.bedwars.data.game.spawners.BedWarsResourceSpawnerType;
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
        Optional<BedWarsMap> optionalGameMap = mapManager.getMapByName(mapName).join();
        if (optionalGameMap.isEmpty()) {
            user.sendMessage("command_map_not_found");
            return;
        }

        String resourceName = args[1].toUpperCase();
        BedWarsResourceSpawnerType resourceSpawnerType;
        try {
            resourceSpawnerType = BedWarsResourceSpawnerType.valueOf(resourceName);
        } catch (IllegalArgumentException e) {
            user.sendMessage("command_map_invalid_resource_type", Arrays.toString(BedWarsResourceSpawnerType.values()));
            return;
        }

        BedWarsMap map = optionalGameMap.get();
        map.addSpawnerLocation(resourceSpawnerType, new BedWarsMapDirectedLocation(player.getLocation()));

        if (!mapManager.saveMap(map).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_spawner_added");
    }
}
