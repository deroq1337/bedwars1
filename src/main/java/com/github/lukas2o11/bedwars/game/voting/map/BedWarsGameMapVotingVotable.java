package com.github.lukas2o11.bedwars.game.voting.map;

import com.github.lukas2o11.bedwars.game.map.DefaultBedWarsGameMap;
import com.github.lukas2o11.bedwars.game.voting.BedWarsGameVotingVotable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class BedWarsGameMapVotingVotable implements BedWarsGameVotingVotable<DefaultBedWarsGameMap> {

    @NotNull
    private final DefaultBedWarsGameMap value;

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
