package com.github.lukas2o11.bedwars.game.countdown;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
    public void onSpecialTick() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 3f, 3f);
            player.sendMessage("§aDas Spiel beginnt in §e" + getCurrent() + " Sekunden");
        });
    }
}
