package com.github.lukas2o11.bedwars.game.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class ItemBuilder<M extends ItemMeta> {

    private final ItemStack item;
    private final M itemMeta;

    public ItemBuilder(@NotNull Material material) {
        this.item = new ItemStack(material);
        this.itemMeta = (M) item.getItemMeta();
    }

    public ItemBuilder<M> material(@NotNull Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder<M> title(@NotNull String title) {
        itemMeta.setDisplayName(title);
        return this;
    }

    public ItemBuilder<M> amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder<M> unbreakable() {
        itemMeta.setUnbreakable(true);
        return this;
    }

    public ItemBuilder<M> lore(@NotNull String... lore) {
        itemMeta.setLore(Arrays.stream(lore).toList());
        return this;
    }

    public ItemBuilder<M> enchant(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder<M> glow() {
        itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder<M> flag(ItemFlag flag) {
        itemMeta.addItemFlags(flag);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(itemMeta);
        return item;
    }
}
