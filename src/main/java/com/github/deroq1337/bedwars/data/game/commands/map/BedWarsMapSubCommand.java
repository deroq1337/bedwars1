package com.github.deroq1337.bedwars.data.game.commands.map;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.map.BedWarsMapManager;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class BedWarsMapSubCommand {

    protected final @NotNull BedWarsGame game;
    protected final @NotNull BedWarsMapManager mapManager;
    private final @NotNull String name;

    public BedWarsMapSubCommand(@NotNull BedWarsGame game, @NotNull String name) {
        this.game = game;
        this.mapManager = game.getMapManager();
        this.name = name;
    }

    protected abstract void execute(@NotNull BedWarsUser user, @NotNull Player player, @NotNull String[] args);
}
