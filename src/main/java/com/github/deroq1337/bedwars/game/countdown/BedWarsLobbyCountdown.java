package com.github.deroq1337.bedwars.game.countdown;

import com.github.deroq1337.bedwars.game.BedWarsGame;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class BedWarsLobbyCountdown extends BedWarsCountdown {

    public BedWarsLobbyCountdown(@NotNull BedWarsGame game) {
        super(game, 60, 20, 60, 30, 10, 5, 4, 3, 2, 1);
    }

    @Override
    public void onTick() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setExp((float) getCurrent() / getStart());
            player.setLevel(getCurrent());
        });
    }

    @Override
    public void onSpecialTick(int tick) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("§aDas Spiel beginnt in §e" + getCurrent() + " Sekunden");
        });
    }
}
