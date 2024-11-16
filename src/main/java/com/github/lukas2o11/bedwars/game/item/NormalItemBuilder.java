package com.github.lukas2o11.bedwars.game.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class NormalItemBuilder extends ItemBuilder<ItemMeta> {

    public NormalItemBuilder(@NotNull Material material) {
        super(material);
    }
}
