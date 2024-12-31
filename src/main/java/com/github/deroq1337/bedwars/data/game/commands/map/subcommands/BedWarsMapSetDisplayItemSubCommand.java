package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMap;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsMapSetDisplayItemSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapSetDisplayItemSubCommand(@NotNull BedWarsGame game) {
        super(game, "setItem");
    }

    @Override
    protected void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 2) {
            user.sendMessage("command_map_set_item_syntax");
            return;
        }

        String mapName = args[0];
        Optional<BedWarsMap> optionalGameMap = mapManager.getMapByName(mapName).join();
        if (optionalGameMap.isEmpty()) {
            user.sendMessage("command_map_not_found");
            return;
        }

        String materialName = args[1].toUpperCase();
        Material material;
        try {
            material = Material.valueOf(materialName);
        } catch (IllegalArgumentException e) {
            user.sendMessage("command_map_invalid_material");
            return;
        }

        BedWarsMap map = optionalGameMap.get();
        map.setDisplayItem(material);

        if (!mapManager.saveMap(map).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_item_set");
    }
}