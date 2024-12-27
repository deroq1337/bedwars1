package com.github.deroq1337.bedwars.data.game.voting;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.exceptions.GameVotingInitializationException;
import com.github.deroq1337.bedwars.data.game.exceptions.GameVotingWinnerDeterminationException;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
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

    private static final int INVENTORY_SIZE = 9;

    protected final @NotNull BedWarsGame game;
    private final @NotNull String name;
    private final @NotNull List<C> candidates;
    private final int slot;
    private final @NotNull String inventoryTitle;
    private final int[] inventorySlots;

    private Optional<C> winner;

    public abstract @NotNull ItemStack getDisplayItem();

    public @NotNull Inventory getInventory() {
        if (inventorySlots.length < candidates.size()) {
            throw new GameVotingInitializationException("Not enough slots (" + inventorySlots.length + ") defined for the number of candidates (" + candidates.size() + ")");
        }

        Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, inventoryTitle);
        IntStream.range(0, candidates.size()).forEach(i -> inventory.setItem(inventorySlots[i], candidates.get(i).getDisplayItem()));
        return inventory;
    }

    public boolean handleInventoryClick(@NotNull BedWarsUser user, @NotNull InventoryClickEvent event) {
        return game.getGameState().map(gameState -> {
            if (!event.getView().getTitle().equals(inventoryTitle)) {
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
                return getCandidateByItem(item).map(candidate -> {
                    UUID uuid = user.getUuid();
                    if (!candidate.getVotes().add(uuid)) {
                        candidate.getVotes().remove(uuid);
                        user.sendMessage("voting_already_voted", candidate.getDisplayTitle());
                        return true;
                    }

                    candidates.stream()
                            .filter(c -> c.getVotes().contains(uuid))
                            .findFirst()
                            .ifPresent(c -> c.getVotes().remove(uuid));
                    candidate.getVotes().add(uuid);
                    user.sendMessage("voting_added", candidate.getDisplayTitle());
                    event.getWhoClicked().closeInventory();
                    return true;
                });
            });
            return true;
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

    private Optional<C> getCandidateByItem(@NotNull ItemStack item) {
        return Optional.ofNullable(item.getItemMeta()).flatMap(itemMeta -> {
            return candidates.stream()
                    .filter(candidate -> candidate.getDisplayItem().getType() == item.getType() && candidate.getDisplayTitle().equals(item.getItemMeta().getDisplayName()))
                    .findFirst();
        });
    }
}
