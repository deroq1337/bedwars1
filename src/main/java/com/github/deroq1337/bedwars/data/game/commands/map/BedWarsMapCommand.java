package com.github.deroq1337.bedwars.data.game.commands.map;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.commands.map.subcommands.*;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BedWarsMapCommand implements CommandExecutor {

    private final @NotNull BedWarsGame game;
    private final @NotNull Map<String, BedWarsMapSubCommand> subCommandMap;

    public BedWarsMapCommand(@NotNull BedWarsGame game) {
        this.game = game;
        this.subCommandMap = Stream.of(
                new BedWarsMapCreateSubCommand(game),
                new BedWarsMapDeleteSubCommand(game),
                new BedWarsMapSetNameSubCommand(game),
                new BedWarsMapAddShopSubCommand(game),
                new BedWarsMapAddSpawnerSubCommand(game),
                new BedWarsMapRemoveShopSubCommand(game),
                new BedWarsMapRemoveSpawnerSubCommand(game),
                new BedWarsMapSetDisplayItemSubCommand(game),
                new BedWarsMapSetRespawnLocationSubCommand(game),
                new BedWarsMapSetSpectatorLocationSubCommand(game),
                new BedWarsMapSetTeamBedSubCommand(game),
                new BedWarsMapSetTeamSpawnSubCommand(game)
        ).collect(Collectors.toMap(subCommand -> subCommand.getName().toLowerCase(), subCommand -> subCommand));

        Optional.ofNullable(game.getBedWars().getCommand("map")).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        Optional<BedWarsUser> optionalUser = game.getUserRegistry().getUser(player.getUniqueId());
        if (optionalUser.isEmpty()) {
            player.sendMessage("§cAn error occurred. Rejoin or contact an administrator.");
            return true;
        }

        BedWarsUser user = optionalUser.get();
        if (!player.hasPermission("bedwars.map")) {
            user.sendMessage("no_permission");
            return true;
        }

        if (args.length < 1) {
            user.sendMessage("command_not_found");
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        Optional<BedWarsMapSubCommand> subCommand = Optional.ofNullable(subCommandMap.get(subCommandName));
        if (subCommand.isEmpty()) {
            user.sendMessage("command_not_found");
            return true;
        }

        String[] subCommandArgs = Arrays.stream(args).skip(1).toArray(String[]::new);
        subCommand.get().execute(user, player, subCommandArgs);
        return true;
    }
}
