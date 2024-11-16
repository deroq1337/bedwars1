package com.github.lukas2o11.bedwars.game.voting;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.NoSuchElementException;

public interface BedWarsGameVoting<T, C extends BedWarsGameVotingCandidate<T>> {

    @NotNull Material getDisplayItem();

    @NotNull String getDisplayTitle();

    int getSlot();

    @NotNull List<C> getCandidates();

    default @NotNull C getWinner() {
        return getCandidates().stream()
                .max((o1, o2) -> Integer.compare(o2.getVotes(), o1.getVotes()))
                .orElseThrow(() -> new NoSuchElementException("No candidates available"));
    }

    default @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, getDisplayTitle());
        getCandidates().forEach(candidate -> inventory.setItem(candidate.getSlot(), new ItemStack(candidate.getDisplayItem())));
        return inventory;
    }
}
