package com.github.deroq1337.bedwars.data.game.voting.freezer;

import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVoting;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public class BedWarsGameFreezerVoting implements BedWarsGameVoting<Boolean, BedWarsGameFreezerVotingVotable, BedWarsGameFreezerVotingCandidate> {

    private static final ItemStack FREEZER_PRO_DISPLAY_ITEM = ItemBuilders.normal(Material.GREEN_DYE)
            .title("§aJa")
            .lore("§7Votes: §e0")
            .build();

    private static final ItemStack FREEZER_CONTRA_DISPLAY_ITEM = ItemBuilders.normal(Material.RED_DYE)
            .title("§cNein")
            .lore("§7Votes: §e0")
            .build();

    private final @NotNull List<BedWarsGameFreezerVotingCandidate> candidates;
    private final @NotNull ItemStack displayItem;
    private final int slot;
    private final @NotNull String inventoryTitle;

    public BedWarsGameFreezerVoting() {
        this.candidates = Arrays.asList(
                new BedWarsGameFreezerVotingCandidate(true, FREEZER_PRO_DISPLAY_ITEM, 12),
                new BedWarsGameFreezerVotingCandidate(false, FREEZER_CONTRA_DISPLAY_ITEM, 14)
        );
        this.displayItem = buildDisplayItem();
        this.slot = 14;
        this.inventoryTitle = "§8Freezer-Voting";
    }

    @Override
    public @NotNull String getWinnerAsString() {
        return getWinner().map(winner -> Optional.ofNullable(winner.getVotable().getDisplayItem().getItemMeta())
                        .map(ItemMeta::getDisplayName)
                        .orElse("N/A"))
                .orElse("N/A");
    }

    private @NotNull ItemStack buildDisplayItem() {
        return ItemBuilders.normal(Material.ICE)
                .title("§bFreezer-Voting")
                .lore("§7Aktueller Gewinner: §c" + getWinnerAsString())
                .build();
    }

}
