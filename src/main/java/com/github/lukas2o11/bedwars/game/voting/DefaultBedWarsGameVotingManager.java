package com.github.lukas2o11.bedwars.game.voting;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.lukas2o11.bedwars.game.voting.freezer.BedWarsGameFreezerVoting;
import com.github.lukas2o11.bedwars.game.voting.freezer.BedWarsGameFreezerVotingCandidate;
import com.github.lukas2o11.bedwars.game.voting.map.BedWarsGameMapVoting;
import com.github.lukas2o11.bedwars.game.voting.map.BedWarsGameMapVotingCandidate;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DefaultBedWarsGameVotingManager implements BedWarsGameVotingManager<DefaultBedWarsGameMap> {

    @NotNull
    private final BedWarsGameVoting<DefaultBedWarsGameMap, BedWarsGameMapVotingCandidate> gameMapVoting;

    @NotNull
    private final BedWarsGameVoting<Boolean, BedWarsGameFreezerVotingCandidate> freezerVoting;

    public DefaultBedWarsGameVotingManager(BedWarsGame<DefaultBedWarsGameMap> game) {
        this.gameMapVoting = new BedWarsGameMapVoting(game);
        this.freezerVoting = new BedWarsGameFreezerVoting();
    }

    @Override
    public @NotNull <C extends BedWarsGameVotingCandidate<DefaultBedWarsGameMap>> BedWarsGameVoting<DefaultBedWarsGameMap, C> getGameMapVoting() {
        return (BedWarsGameVoting<DefaultBedWarsGameMap, C>) gameMapVoting;
    }

    @Override
    public @NotNull <C extends BedWarsGameVotingCandidate<Boolean>> BedWarsGameVoting<Boolean, C> getFreezerVoting() {
        return (BedWarsGameVoting<Boolean, C>) freezerVoting;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, "§8Voting");
        Arrays.asList(gameMapVoting, freezerVoting).forEach(voting -> inventory.setItem(voting.getSlot(), new ItemStack(voting.getDisplayItem())));
        return inventory;
    }
}
