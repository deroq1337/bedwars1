package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsMapSetNameSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapSetNameSubCommand(@NotNull BedWarsGame game) {
        super(game, "setName");
    }

    @Override
    protected void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 2) {
            user.sendMessage("command_map_set_name_syntax");
            return;
        }

        String oldName = args[0];
        String newName = args[1];
        Optional<BedWarsGameMap> optionalGameMap = gameMapManager.getMapByName(oldName).join();
        if (optionalGameMap.isEmpty()) {
            user.sendMessage("command_map_not_found");
            return;
        }

        if (gameMapManager.getMapByName(newName).join().isPresent()) {
            user.sendMessage("command_map_already_exists");
            return;
        }

        BedWarsGameMap gameMap = optionalGameMap.get();
        gameMap.setName(newName);

        if (!gameMapManager.saveMap(gameMap).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_name_changed");
    }
}