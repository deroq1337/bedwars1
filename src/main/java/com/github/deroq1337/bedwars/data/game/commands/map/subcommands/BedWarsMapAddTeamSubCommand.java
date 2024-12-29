package com.github.deroq1337.bedwars.data.game.commands.map.subcommands;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.BedWarsMapSubCommand;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMap;
import com.github.deroq1337.bedwars.data.game.team.BedWarsGameTeamType;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class BedWarsMapAddTeamSubCommand extends BedWarsMapSubCommand {

    public BedWarsMapAddTeamSubCommand(@NotNull BedWarsGame game) {
        super(game, "addTeam");
    }

    @Override
    protected void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args) {
        if (args.length < 2) {
            user.sendMessage("command_map_add_team_syntax");
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
        if (gameMap.getTeamCount() >= game.getMainConfig().getTeamCount()) {
            user.sendMessage("command_map_enough_teams_added");
            return;
        }

        if (!gameMap.addTeam(teamType)) {
            user.sendMessage("command_map_team_already_added");
            return;
        }

        if (!gameMapManager.saveMap(gameMap).join()) {
            user.sendMessage("command_map_not_updated");
            return;
        }

        user.sendMessage("command_map_team_added");
    }
}
