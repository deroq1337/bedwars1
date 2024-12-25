package com.github.deroq1337.bedwars.data.game.voting.freezer;

import com.github.deroq1337.bedwars.data.game.voting.BedWarsGameVotingVotable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class BedWarsGameFreezerVotingVotable implements BedWarsGameVotingVotable<Boolean> {

    private final @NotNull Boolean value;
    private final @NotNull ItemStack displayItem;
    private final int slot;

    @Override
    public @NotNull String getDisplayTitle() {
        return Optional.ofNullable(displayItem.getItemMeta())
                .map(ItemMeta::getDisplayName)
                .orElse("N/A");
    }
}
