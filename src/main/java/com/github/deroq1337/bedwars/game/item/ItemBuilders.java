package com.github.deroq1337.bedwars.game.item;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class ItemBuilders {

    public static @NotNull NormalItemBuilder normal(@NotNull Material material) {
        return new NormalItemBuilder(material);
    }
}
