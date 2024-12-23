package com.github.deroq1337.bedwars.game.voting;

import com.github.deroq1337.bedwars.game.BedWarsGame;
import com.github.deroq1337.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.deroq1337.bedwars.game.voting.freezer.BedWarsGameFreezerVoting;
import com.github.deroq1337.bedwars.game.voting.freezer.BedWarsGameFreezerVotingCandidate;
import com.github.deroq1337.bedwars.game.voting.freezer.BedWarsGameFreezerVotingVotable;
import com.github.deroq1337.bedwars.game.voting.map.BedWarsGameMapVoting;
import com.github.deroq1337.bedwars.game.voting.map.BedWarsGameMapVotingCandidate;
import com.github.deroq1337.bedwars.game.voting.map.BedWarsGameMapVotingVotable;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class DefaultBedWarsGameVotingManager implements BedWarsGameVotingManager<DefaultBedWarsGameMap> {

    private static final int INVENTORY_SIZE = 27;

    private final @NotNull BedWarsGameVoting<DefaultBedWarsGameMap, BedWarsGameMapVotingVotable, BedWarsGameMapVotingCandidate> gameMapVoting;
    private final @NotNull BedWarsGameVoting<Boolean, BedWarsGameFreezerVotingVotable, BedWarsGameFreezerVotingCandidate> freezerVoting;

    @Getter
    private final @NotNull String inventoryTitle;

    public DefaultBedWarsGameVotingManager(BedWarsGame<DefaultBedWarsGameMap> game) {
        this.gameMapVoting = new BedWarsGameMapVoting(game);
        this.freezerVoting = new BedWarsGameFreezerVoting();
        this.inventoryTitle = "§8Voting";
    }

    @Override
    public @NotNull <V extends BedWarsGameVotingVotable<DefaultBedWarsGameMap>, C extends BedWarsGameVotingCandidate<DefaultBedWarsGameMap, V>> BedWarsGameVoting<DefaultBedWarsGameMap, V, C> getGameMapVoting() {
        return (BedWarsGameVoting<DefaultBedWarsGameMap, V, C>) gameMapVoting;
    }

    @Override
    public @NotNull <V extends BedWarsGameVotingVotable<Boolean>, C extends BedWarsGameVotingCandidate<Boolean, V>> BedWarsGameVoting<Boolean, V, C> getFreezerVoting() {
        return (BedWarsGameVoting<Boolean, V, C>) freezerVoting;
    }

    @Override
    public @NotNull List<BedWarsGameVoting<?, ? extends BedWarsGameVotingVotable<?>, ? extends BedWarsGameVotingCandidate<?, ? extends BedWarsGameVotingVotable<?>>>> getVotings() {
        return List.of(gameMapVoting, freezerVoting);
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, inventoryTitle);
        Arrays.asList(gameMapVoting, freezerVoting).forEach(voting -> inventory.setItem(voting.getSlot(), new ItemStack(voting.getDisplayItem())));
        return inventory;
    }
}
