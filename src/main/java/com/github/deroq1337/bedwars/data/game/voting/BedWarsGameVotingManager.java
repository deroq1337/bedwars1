package com.github.deroq1337.bedwars.data.game.voting;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface BedWarsGameVotingManager {

    void determineWinners();

    boolean handleInventoryClick(@NotNull InventoryClickEvent event);

    @NotNull Collection<BedWarsGameVoting<?, ? extends BedWarsGameVotingCandidate<?>>> getVotings();

    <T> Optional<BedWarsGameVoting<T, ? extends BedWarsGameVotingCandidate<T>>> getVoting(
            @NotNull Class<? extends BedWarsGameVoting<T, ? extends BedWarsGameVotingCandidate<T>>> votingClass, @NotNull Class<T> valueClass);

    Optional<BedWarsGameVoting<?, ? extends BedWarsGameVotingCandidate<?>>> getVoting(
            @NotNull Class<? extends BedWarsGameVoting<?, ? extends BedWarsGameVotingCandidate<?>>> votingClass);

    Optional<BedWarsGameVoting<?, ? extends BedWarsGameVotingCandidate<?>>> getVotingByItem(@NotNull ItemStack item);

    <T> Optional<BedWarsGameVotingCandidate<T>> getVotingWinner(
            @NotNull Class<? extends BedWarsGameVoting<T, ? extends BedWarsGameVotingCandidate<T>>> votingClass, @NotNull Class<T> valueClass);

    Map<Class<? extends BedWarsGameVoting>, BedWarsGameVotingCandidate<?>> getVotingWinnerMap();

    @NotNull ItemStack getVotingItem();

    @NotNull Inventory getVotingInventory();
}
