package com.github.lukas2o11.bedwars.game.teams;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

@RequiredArgsConstructor
@Getter
public enum BedWarsGameTeamType {

    RED(Material.RED_WOOL, "§c", "Rot"),
    BLUE(Material.BLUE_WOOL, "§9", "Blau"),
    GREEN(Material.GREEN_WOOL, "§2", "Grün"),
    YELLOW(Material.YELLOW_WOOL, "§e", "Gelb"),
    ORANGE(Material.ORANGE_WOOL, "§6", "Orange"),
    PURPLE(Material.PURPLE_WOOL, "§5", "Lila"),
    PINK(Material.PINK_WOOL, "§d", "Pink"),
    CYAN(Material.CYAN_WOOL, "§b", "Türkis");

    private final Material material;
    private final String colorCode;
    private final String name;


}
