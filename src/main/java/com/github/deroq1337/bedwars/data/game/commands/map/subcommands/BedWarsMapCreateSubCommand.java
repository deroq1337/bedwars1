package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BedWarsMapCreateSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapCreateSubCommand(@NotNull BedWarsGame game) {
        super(game, "create");
    }

    @Override
    protected void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 1) {
            user.sendMessage("command_map_create_syntax");
            return;
        }

        String mapName = args[0];
        if (gameMapManager.getMapByName(mapName).join().isPresent()) {
            user.sendMessage("command_map_already_exists");
            return;
        }

        if (!gameMapManager.createMap(BedWarsGameMap.create(mapName)).join()) {
            user.sendMessage("command_map_not_created");
            return;
        }

        user.sendMessage("command_map_created");
    }
}
