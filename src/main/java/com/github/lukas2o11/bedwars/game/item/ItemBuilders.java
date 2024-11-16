package com.github.lukas2o11.bedwars.game.item;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class ItemBuilders {

    public static NormalItemBuilder normal(@NotNull Material material) {
        return new NormalItemBuilder(material);
    }
}
