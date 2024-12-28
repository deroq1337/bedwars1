package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsMapSetRespawnLocationSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapSetRespawnLocationSubCommand(@NotNull BedWarsGame game) {
        super(game, "setRespawn");
    }

    @Override
    protected void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 1) {
            user.sendMessage("command_map_set_respawn_syntax");
            return;
        }

        String mapName = args[0];
        Optional<BedWarsGameMap> optionalGameMap = gameMapManager.getMapByName(mapName).join();
        if (optionalGameMap.isEmpty()) {
            user.sendMessage("command_map_not_found");
            return;
        }

        BedWarsGameMap gameMap = optionalGameMap.get();
        gameMap.setRespawnLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));

        if (!gameMapManager.updateMap(gameMap).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_respawn_set");
    }
}
