package com.github.deroq1337.bedwars.data.game.voting;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import com.github.deroq1337.bedwars.data.game.voting.map.BedWarsGameMapVoting;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class DefaultBedWarsVotingManager implements BedWarsVotingManager {

    private final @NotNull BedWarsGame game;
    private final @NotNull Map<Class<? extends BedWarsVoting>, BedWarsVoting<?, ? extends BedWarsVotingCandidate<?>>> votingMap = new ConcurrentHashMap<>();
    private final @NotNull Map<Class<? extends BedWarsVoting>, BedWarsVotingCandidate<?>> votingWinnerMap = new ConcurrentHashMap<>();

    public DefaultBedWarsVotingManager(@NotNull BedWarsGame game) {
        this.game = game;
        votingMap.put(BedWarsGameMapVoting.class, new BedWarsGameMapVoting(game));
    }

    @Override
    public void clearVotes(@NotNull UUID player) {
        getVotings().forEach(voting -> voting.getCandidates().stream()
                .filter(candidate -> candidate.getVotes().contains(player))
                .forEach(candidate -> candidate.getVotes().remove(player)));
    }

    @Override
    public void resetWinners() {
        getVotings().forEach(BedWarsVoting::resetWinner);
    }

    @Override
    public void determineWinners() {
        getVotings().forEach(voting -> votingWinnerMap.put(voting.getClass(), voting.determineWinner()));
    }

    @Override
    public boolean handleInventoryClick(@NotNull BedWarsUser user, @NotNull InventoryClickEvent event) {
        return game.getGameState().map(gameState -> {
            if (!event.getView().getTitle().equals(getInventoryTitle(user))) {
                return false;
            }

            event.setCancelled(true);
            if (!(gameState instanceof BedWarsLobbyState lobbyState)) {
                return true;
            }

            if (lobbyState.getCountdown().getCurrent() <= 10) {
                return true;
            }

            Optional.ofNullable(event.getCurrentItem()).flatMap(item -> {
                return getVotingByItem(user, item).map(voting -> {
                    if (voting.canVote()) {
                        event.getWhoClicked().openInventory(voting.getInventory(user));
                    }
                    return true;
                });
            });
            return true;
        }).orElse(false);
    }

    @Override
    public @NotNull Collection<BedWarsVoting<?, ? extends BedWarsVotingCandidate<?>>> getVotings() {
        return votingMap.values();
    }

    @Override
    public <T> Optional<BedWarsVoting<T, ? extends BedWarsVotingCandidate<T>>> getVoting(
            @NotNull Class<? extends BedWarsVoting<T, ? extends BedWarsVotingCandidate<T>>> votingClass, @NotNull Class<T> valueClass) {
        return Optional.ofNullable((BedWarsVoting<T, ? extends BedWarsVotingCandidate<T>>) votingMap.get(votingClass));
    }

    @Override
    public Optional<BedWarsVoting<?, ? extends BedWarsVotingCandidate<?>>> getVoting(
            @NotNull Class<? extends BedWarsVoting<?, ? extends BedWarsVotingCandidate<?>>> votingClass) {
        return Optional.ofNullable(votingMap.get(votingClass));
    }

    @Override
    public Optional<BedWarsVoting<?, ? extends BedWarsVotingCandidate<?>>> getVotingByItem(@NotNull BedWarsUser user, @NotNull ItemStack item) {
        return getVotings().stream()
                .filter(voting -> voting.getDisplayItem(user).getType() == item.getType())
                .findFirst();
    }

    @Override
    public <T> Optional<BedWarsVotingCandidate<T>> getVotingWinner(
            @NotNull Class<? extends BedWarsVoting<T, ? extends BedWarsVotingCandidate<T>>> votingClass, @NotNull Class<T> valueClass) {
        return Optional.ofNullable((BedWarsVotingCandidate<T>) votingWinnerMap.get(votingClass));
    }

    @Override
    public @NotNull ItemStack getItem(@NotNull BedWarsUser user) {
        return ItemBuilders.normal(Material.MAP)
                .title(user.getMessage("voting_item_name"))
                .build();
    }

    @Override
    public @NotNull Inventory getInventory(@NotNull BedWarsUser user) {
        Inventory inventory = Bukkit.createInventory(null, 9, getInventoryTitle(user));
        getVotings().forEach(voting -> inventory.setItem(voting.getSlot(), voting.getDisplayItem(user)));
        return inventory;
    }

    private @NotNull String getInventoryTitle(@NotNull BedWarsUser user) {
        return user.getMessage("voting_inventory_title");
    }
}
