package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BedWarsMapDeleteSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapDeleteSubCommand(@NotNull BedWarsGame game) {
        super(game, "delete");
    }

    @Override
    protected void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 1) {
            user.sendMessage("command_map_delete_syntax");
            return;
        }

        String mapName = args[0];
        if (gameMapManager.getMapByName(mapName).join().isEmpty()) {
            user.sendMessage("command_map_not_found");
            return;
        }

        if (!gameMapManager.deleteMap(mapName).join()) {
            user.sendMessage("command_map_not_deleted");
            return;
        }

        user.sendMessage("command_map_deleted");
    }
}
