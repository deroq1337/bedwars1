package com.github.deroq1337.bedwars.data.game.commands.map;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.map.BedWarsGameMapManager;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class BedWarsMapSubCommand {

    protected final @NotNull BedWarsGame game;
    protected final @NotNull BedWarsGameMapManager gameMapManager;
    private final @NotNull String name;

    public BedWarsMapSubCommand(@NotNull BedWarsGame game, @NotNull String name) {
        this.game = game;
        this.gameMapManager = game.getGameMapManager();
        this.name = name;
    }

    protected abstract void execute(@NotNull BedWarsGameUser user, @NotNull Player player, @NotNull String[] args);
}
