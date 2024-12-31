package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMap;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsMapDirectedLocation;
import com.github.deroq1337.bedwars.data.game.team.BedWarsTeamType;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class BedWarsMapSetTeamSpawnSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapSetTeamSpawnSubCommand(@NotNull BedWarsGame game) {
        super(game, "setTeamSpawn");
    }

    @Override
    protected void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 2) {
            user.sendMessage("command_map_set_team_spawn_syntax");
            return;
        }

        String mapName = args[0];
        Optional<BedWarsMap> optionalGameMap = mapManager.getMapByName(mapName).join();
        if (optionalGameMap.isEmpty()) {
            user.sendMessage("command_map_not_found");
            return;
        }

        String teamName = args[1].toUpperCase();
        BedWarsTeamType teamType;
        try {
            teamType = BedWarsTeamType.valueOf(teamName);
        } catch (IllegalArgumentException e) {
            user.sendMessage("command_map_invalid_team", Arrays.toString(BedWarsTeamType.values()));
            return;
        }

        if (!game.getMainConfig().getTeams().contains(teamType)) {
            user.sendMessage("command_map_team_not_added");
            return;
        }

        BedWarsMap map = optionalGameMap.get();
        map.addTeamSpawnLocation(teamType, new BedWarsMapDirectedLocation(player.getLocation()));
        if (!mapManager.saveMap(map).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_team_spawn_set");
    }
}
