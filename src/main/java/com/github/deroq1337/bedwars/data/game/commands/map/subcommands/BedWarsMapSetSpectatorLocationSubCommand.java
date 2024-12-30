package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsMapSetSpectatorLocationSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapSetSpectatorLocationSubCommand(@NotNull BedWarsGame game) {
        super(game, "setSpectator");
    }

    @Override
    protected void execute(@NotNull BedWarsGameUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 1) {
            user.sendMessage("command_map_set_spectator_syntax");
            return;
        }

        String mapName = args[0];
        Optional<BedWarsGameMap> optionalGameMap = gameMapManager.getMapByName(mapName).join();
        if (optionalGameMap.isEmpty()) {
            user.sendMessage("command_map_not_found");
            return;
        }

        BedWarsGameMap gameMap = optionalGameMap.get();
        gameMap.setSpectatorLocation(new BedWarsDirectedGameMapLocation(player.getLocation()));

        if (!gameMapManager.saveMap(gameMap).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_spectator_set");
    }
}
