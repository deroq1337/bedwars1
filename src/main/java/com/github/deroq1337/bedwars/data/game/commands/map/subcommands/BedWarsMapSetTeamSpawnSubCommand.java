package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.data.game.team.BedWarsGameTeamType;
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
        Optional<BedWarsGameMap> optionalGameMap = gameMapManager.getMapByName(mapName).join();
        if (optionalGameMap.isEmpty()) {
            user.sendMessage("command_map_not_found");
            return;
        }

        String teamName = args[1].toUpperCase();
        BedWarsGameTeamType teamType;
        try {
            teamType = BedWarsGameTeamType.valueOf(teamName);
        } catch (IllegalArgumentException e) {
            user.sendMessage("command_map_invalid_team", Arrays.toString(BedWarsGameTeamType.values()));
            return;
        }

        BedWarsGameMap gameMap = optionalGameMap.get();
        if (!gameMap.hasTeam(teamType)) {
            user.sendMessage("command_map_team_not_added");
            return;
        }

        gameMap.addTeamSpawnLocation(teamType, new BedWarsDirectedGameMapLocation(player.getLocation()));
        if (!gameMapManager.updateMap(gameMap).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_team_spawn_set");
    }
}