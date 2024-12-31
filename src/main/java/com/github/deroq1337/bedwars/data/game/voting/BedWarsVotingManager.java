package com.github.deroq1337.bedwars.data.game.voting;

import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface BedWarsVotingManager {

    void clearVotes(@NotNull UUID player);

    void resetWinners();

    void determineWinners();

    boolean handleInventoryClick(@NotNull BedWarsUser user, @NotNull InventoryClickEvent event);

    @NotNull Collection<BedWarsVoting<?, ? extends BedWarsVotingCandidate<?>>> getVotings();

    <T> Optional<BedWarsVoting<T, ? extends BedWarsVotingCandidate<T>>> getVoting(
            @NotNull Class<? extends BedWarsVoting<T, ? extends BedWarsVotingCandidate<T>>> votingClass, @NotNull Class<T> valueClass);

    Optional<BedWarsVoting<?, ? extends BedWarsVotingCandidate<?>>> getVoting(
            @NotNull Class<? extends BedWarsVoting<?, ? extends BedWarsVotingCandidate<?>>> votingClass);

    Optional<BedWarsVoting<?, ? extends BedWarsVotingCandidate<?>>> getVotingByItem(@NotNull BedWarsUser user, @NotNull ItemStack item);

    <T> Optional<BedWarsVotingCandidate<T>> getVotingWinner(
            @NotNull Class<? extends BedWarsVoting<T, ? extends BedWarsVotingCandidate<T>>> votingClass, @NotNull Class<T> valueClass);

    Map<Class<? extends BedWarsVoting>, BedWarsVotingCandidate<?>> getVotingWinnerMap();

    @NotNull ItemStack getItem(@NotNull BedWarsUser user);

    @NotNull Inventory getInventory(@NotNull BedWarsUser user);
}
