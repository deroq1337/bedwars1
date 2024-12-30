package com.github.deroq1337.bedwars.data.game.voting;

import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface BedWarsGameVotingManager {

    void clearVotes(@NotNull UUID player);

    void resetWinners();

    void determineWinners();

    boolean handleInventoryClick(@NotNull BedWarsGameUser user, @NotNull InventoryClickEvent event);

    @NotNull Collection<BedWarsGameVoting<?, ? extends BedWarsGameVotingCandidate<?>>> getVotings();

    <T> Optional<BedWarsGameVoting<T, ? extends BedWarsGameVotingCandidate<T>>> getVoting(
            @NotNull Class<? extends BedWarsGameVoting<T, ? extends BedWarsGameVotingCandidate<T>>> votingClass, @NotNull Class<T> valueClass);

    Optional<BedWarsGameVoting<?, ? extends BedWarsGameVotingCandidate<?>>> getVoting(
            @NotNull Class<? extends BedWarsGameVoting<?, ? extends BedWarsGameVotingCandidate<?>>> votingClass);

    Optional<BedWarsGameVoting<?, ? extends BedWarsGameVotingCandidate<?>>> getVotingByItem(@NotNull BedWarsGameUser user, @NotNull ItemStack item);

    <T> Optional<BedWarsGameVotingCandidate<T>> getVotingWinner(
            @NotNull Class<? extends BedWarsGameVoting<T, ? extends BedWarsGameVotingCandidate<T>>> votingClass, @NotNull Class<T> valueClass);

    Map<Class<? extends BedWarsGameVoting>, BedWarsGameVotingCandidate<?>> getVotingWinnerMap();

    @NotNull ItemStack getItem(@NotNull BedWarsGameUser user);

    @NotNull Inventory getInventory(@NotNull BedWarsGameUser user);
}
