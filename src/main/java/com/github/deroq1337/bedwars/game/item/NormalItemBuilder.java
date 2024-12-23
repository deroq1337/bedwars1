package com.github.deroq1337.bedwars.game.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class NormalItemBuilder extends ItemBuilder<ItemMeta> {

    public NormalItemBuilder(@NotNull Material material) {
        super(material);
    }
}
