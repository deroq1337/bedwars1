package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.data.game.team.BedWarsGameTeamType;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class BedWarsMapSetTeamBedSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapSetTeamBedSubCommand(@NotNull BedWarsGame game) {
        super(game, "setTeamBed");
    }

    @Override
    protected void execute(@NotNull BedWarsGameUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 2) {
            user.sendMessage("command_map_set_team_bed_syntax");
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

        gameMap.addTeamBedLocation(teamType, new BedWarsDirectedGameMapLocation(player.getTargetBlock(null, 6).getLocation()));
        if (!gameMapManager.saveMap(gameMap).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_team_bed_set");
    }
}
