package com.github.deroq1337.bedwars.data.game.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

@RequiredArgsConstructor
@Getter
public enum BedWarsGameTeamType {

    RED(Material.RED_WOOL, "team_red_color", "team_red_name"),
    BLUE(Material.BLUE_WOOL, "team_blue_color", "team_blue_name"),
    GREEN(Material.GREEN_WOOL, "team_green_color", "team_green_name"),
    YELLOW(Material.YELLOW_WOOL, "team_yellow_color", "team_yellow_name"),
    ORANGE(Material.ORANGE_WOOL, "team_orange_color", "team_orange_name"),
    PURPLE(Material.PURPLE_WOOL, "team_purple_color", "team_purple_name"),
    PINK(Material.PINK_WOOL, "team_pink_color", "team_pink_name"),
    CYAN(Material.CYAN_WOOL, "team_cyan_color", "team_cyan_name");

    private final Material material;
    private final String colorCode;
    private final String name;
}
