package com.github.deroq1337.bedwars.data.game.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {

    public static final ItemStack TEAM_SELECTOR_ITEM = ItemBuilders.normal(Material.RED_BED)
            .title("§cTeamauswahl")
            .build();

    public static final ItemStack VOTING_ITEM_ITEM = ItemBuilders.normal(Material.MAP)
            .title("§eVoting")
            .build();

    public static final ItemStack ACHIEVEMENT_ITEM = ItemBuilders.normal(Material.NETHER_STAR)
            .title("§bAchievements")
            .glow()
            .build();

    public static final ItemStack LOBBY_ITEM = ItemBuilders.normal(Material.SLIME_BALL)
            .title("§aZur Lobby")
            .build();
}
