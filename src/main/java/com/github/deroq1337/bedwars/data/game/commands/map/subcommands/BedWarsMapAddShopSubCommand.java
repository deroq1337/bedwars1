package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMap;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsMapDirectedLocation;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BedWarsMapAddShopSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapAddShopSubCommand(@NotNull BedWarsGame game) {
        super(game, "addShop");
    }

    @Override
    protected void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 1) {
            user.sendMessage("command_map_add_shop_syntax");
            return;
        }

        String mapName = args[0];
        Optional<BedWarsMap> optionalGameMap = mapManager.getMapByName(mapName).join();
        if (optionalGameMap.isEmpty()) {
            user.sendMessage("command_map_not_found");
            return;
        }

        BedWarsMap map = optionalGameMap.get();
        int id = map.addShopLocation(new BedWarsMapDirectedLocation(player.getLocation()));

        if (!mapManager.saveMap(map).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_shop_added", id);
    }
}