package com.github.deroq1337.bedwars.data.game.voting;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.exceptions.GameVotingInitializationException;
import com.github.deroq1337.bedwars.data.game.exceptions.GameVotingWinnerDeterminationException;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Getter
public abstract class BedWarsGameVoting<T, C extends BedWarsGameVotingCandidate<T>> {

    protected final @NotNull BedWarsGame game;
    private final @NotNull List<C> candidates;
    private final int slot;
    private final int inventorySize;
    private final List<Integer> inventorySlots;

    private Optional<C> winner;

    public abstract @NotNull String getName(@NotNull BedWarsGameUser user);

    public abstract @NotNull String getInventoryTitle(@NotNull BedWarsGameUser user);

    public abstract @NotNull ItemStack getDisplayItem(@NotNull BedWarsGameUser user);

    public boolean canVote() {
        return game.getGameState()
                .map(gameState -> gameState instanceof BedWarsLobbyState && gameState.getCountdown().getCurrent() > 10)
                .orElse(false);
    }

    public @NotNull Inventory getInventory(@NotNull BedWarsGameUser user) {
        int slotsSize = getInventorySlots().size();
        if (slotsSize < candidates.size()) {
            throw new GameVotingInitializationException("Not enough slots (" + slotsSize + ") defined for the number of candidates (" + candidates.size() + ")");
        }

        Inventory inventory = Bukkit.createInventory(null, getInventorySize(), getInventoryTitle(user));
        IntStream.range(0, candidates.size()).forEach(i -> inventory.setItem(inventorySlots.get(i), candidates.get(i).getDisplayItem(user)));
        return inventory;
    }

    public boolean handleInventoryClick(@NotNull BedWarsGameUser user, @NotNull InventoryClickEvent event) {
        return game.getGameState().map(gameState -> {
            if (!event.getView().getTitle().equals(getInventoryTitle(user))) {
                return false;
            }

            event.setCancelled(true);
            if (!canVote()) {
                return true;
            }

            return Optional.ofNullable(event.getCurrentItem()).flatMap(item -> {
                return getCandidateByItem(user, item).map(candidate -> {
                    UUID uuid = user.getUuid();
                    if (!candidate.getVotes().add(uuid)) {
                        candidate.getVotes().remove(uuid);
                        user.sendMessage("voting_vote_removed", candidate.getDisplayTitle(user));
                        return true;
                    }

                    candidates.stream()
                            .filter(c -> c.getVotes().contains(uuid))
                            .findFirst()
                            .ifPresent(c -> c.getVotes().remove(uuid));
                    candidate.getVotes().add(uuid);
                    user.sendMessage("voting_vote_added", candidate.getDisplayTitle(user));
                    event.getWhoClicked().closeInventory();
                    return true;
                });
            }).orElse(true);
        }).orElse(false);
    }

    public @NotNull C determineWinner() {
        Optional<C> winner = getCurrentWinner();
        if (winner.isEmpty()) {
            throw new GameVotingWinnerDeterminationException("Could not determine winner: candidates are empty");
        }

        this.winner = winner;
        return winner.get();
    }

    public Optional<C> getCurrentWinner() {
        return candidates.stream()
                .max((o1, o2) -> -Integer.compare(o2.getVotes().size(), o1.getVotes().size()))
                .stream()
                .findFirst();
    }

    public void resetWinner() {
        this.winner = Optional.empty();
    }

    private Optional<C> getCandidateByItem(@NotNull BedWarsGameUser user, @NotNull ItemStack item) {
        return Optional.ofNullable(item.getItemMeta()).flatMap(itemMeta -> {
            return candidates.stream()
                    .filter(candidate -> candidate.getDisplayItem(user).getType() == item.getType() && candidate.getDisplayTitle(user).equals(item.getItemMeta().getDisplayName()))
                    .findFirst();
        });
    }
}
