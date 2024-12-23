package com.github.deroq1337.bedwars.game.voting.map;

import com.github.deroq1337.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.deroq1337.bedwars.game.voting.BedWarsGameVotingVotable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class BedWarsGameMapVotingVotable implements BedWarsGameVotingVotable<DefaultBedWarsGameMap> {

    private @NotNull final DefaultBedWarsGameMap value;
    private @NotNull final ItemStack displayItem;
    private final int slot;

    @Override
    public @NotNull String getDisplayTitle() {
        return Optional.ofNullable(displayItem.getItemMeta())
                .map(ItemMeta::getDisplayName)
                .orElse("N/A");
    }
}