package com.github.lukas2o11.bedwars.game.voting;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface BedWarsGameVoting<T, C extends BedWarsGameVotingCandidate<T>> {

    @NotNull Material getDisplayItem();

    @NotNull String getDisplayTitle();

    @NotNull String getInventoryTitle();

    int getSlot();

    @NotNull List<C> getCandidates();

    default @NotNull C getWinner() {
        return getCandidates().stream()
                .max((o1, o2) -> Integer.compare(o2.getVotes(), o1.getVotes()))
                .orElseThrow(() -> new NoSuchElementException("No candidates available"));
    }

    default @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, getInventoryTitle());
        getCandidates().forEach(candidate -> inventory.setItem(candidate.getSlot(), new ItemStack(candidate.getDisplayItem())));
        return inventory;
    }

    default void handleInventoryClick(@NotNull InventoryClickEvent event, @NotNull Player player) {
        Optional.ofNullable(event.getCurrentItem()).ifPresent(item -> {
            BedWarsGameVotingCandidate<T> candidate = getCandidates().stream()
                    .filter(c -> c.getDisplayItem() == item.getType())
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No voting candidate for item '" + item.getType() + "' found"));
            UUID uuid = player.getUniqueId();
            if (!candidate.addVote(uuid)) {
                assert candidate.removeVote(uuid);
                player.sendMessage("§aDein Vote wurde entfernt");
            } else {
                player.sendMessage("§aDu hast für §e" + candidate.getDisplayItem() + " §agevotet");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 3f);
            }
            player.closeInventory();
        });
    }
}
