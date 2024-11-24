package com.github.lukas2o11.bedwars.game.voting;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface BedWarsGameVoting<T, V extends BedWarsGameVotingVotable<T>, C extends BedWarsGameVotingCandidate<T, V>> {

    @NotNull ItemStack getDisplayItem();

    @NotNull String getInventoryTitle();

    int getSlot();

    @NotNull List<C> getCandidates();

    default void updateDisplayItem() {
        Optional.ofNullable(getDisplayItem().getItemMeta()).ifPresent(itemMeta -> {
            String lore = "§7Aktueller Gewinner: " + getWinnerAsString();
            itemMeta.setLore(Collections.singletonList(lore));
            getDisplayItem().setItemMeta(itemMeta);
        });
    }

    default Optional<C> getWinner() {
        if (getCandidates().isEmpty()) {
            System.out.println("No candidates available: " + getClass().getSimpleName());
            return Optional.empty();
        }

        return getCandidates().stream()
                .max((o1, o2) -> -Integer.compare(o2.getVotes().get(), o1.getVotes().get()));
    }

    @NotNull String getWinnerAsString();

    default @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, getInventoryTitle());
        getCandidates().forEach(candidate -> inventory.setItem(candidate.getVotable().getSlot(), new ItemStack(candidate.getVotable().getDisplayItem())));
        return inventory;
    }

    default void handleInventoryClick(@NotNull InventoryClickEvent event, @NotNull Player player) {
        Optional.ofNullable(event.getCurrentItem()).ifPresent(item -> {
            BedWarsGameVotingCandidate<T, ?> candidate = getCandidates().stream()
                    .filter(c -> c.getVotable().getDisplayItem().getType() == item.getType())
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No voting candidate for item '" + item.getType() + "' found: " + getClass().getSimpleName()));
            UUID uuid = player.getUniqueId();
            if (!candidate.getVotes().add(uuid)) {
                candidate.getVotes().remove(uuid);
                player.sendMessage("§aDein Vote wurde entfernt");
            } else {
                getCandidates().stream()
                        .filter(c -> !c.equals(candidate) && c.getVotes().contains(uuid))
                        .findFirst()
                        .ifPresent(c -> {
                            c.getVotes().remove(uuid);
                            c.updateDisplayItem();
                        });
                player.sendMessage("§aDu hast für " + candidate.getVotable().getDisplayTitle() + " §agevotet");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 3f);
            }

            candidate.updateDisplayItem();
            updateDisplayItem();
            player.closeInventory();
        });
    }
}
