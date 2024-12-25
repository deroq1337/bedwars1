package com.github.deroq1337.bedwars.data.game.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class ItemBuilder<M extends ItemMeta> {

    private final @NotNull ItemStack item;
    private final @NotNull M itemMeta;

    public ItemBuilder(@NotNull Material material) {
        this.item = new ItemStack(material);
        this.itemMeta = (M) item.getItemMeta();
    }

    public @NotNull ItemBuilder<M> material(@NotNull Material material) {
        item.setType(material);
        return this;
    }

    public @NotNull ItemBuilder<M> title(@NotNull String title) {
        itemMeta.setDisplayName(title);
        return this;
    }

    public @NotNull ItemBuilder<M> amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public @NotNull ItemBuilder<M> unbreakable() {
        itemMeta.setUnbreakable(true);
        return this;
    }

    public @NotNull ItemBuilder<M> lore(@NotNull String... lore) {
        itemMeta.setLore(Arrays.stream(lore).toList());
        return this;
    }

    public @NotNull ItemBuilder<M> enchant(@NotNull Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public @NotNull ItemBuilder<M> glow() {
        itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public @NotNull ItemBuilder<M> flag(@NotNull ItemFlag flag) {
        itemMeta.addItemFlags(flag);
        return this;
    }

    public @NotNull ItemStack build() {
        item.setItemMeta(itemMeta);
        return item;
    }
}
