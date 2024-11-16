package com.github.lukas2o11.bedwars.game.voting.freezer;

import com.github.lukas2o11.bedwars.game.voting.BedWarsGameVotingVotable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class BedWarsGameFreezerVotingVotable implements BedWarsGameVotingVotable<Boolean> {

    @NotNull
    private final Boolean value;

    @NotNull
    private final ItemStack displayItem;

    private final int slot;

    @Override
    public String getDisplayTitle() {
        return Optional.ofNullable(displayItem.getItemMeta())
                .map(ItemMeta::getDisplayName)
                .orElse("N/A");
    }
}
