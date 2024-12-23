package com.github.deroq1337.bedwars.game.teams;

import com.github.deroq1337.bedwars.game.item.ItemBuilders;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class DefaultBedWarsGameTeam implements BedWarsGameTeam {

    private @NotNull final BedWarsGameTeamType teamType;
    private @NotNull final Set<UUID> players;
    private @NotNull final ItemStack displayItem;
    private boolean bedDestroyed = false;
    private boolean eliminated = false;

    public DefaultBedWarsGameTeam(@NotNull BedWarsGameTeamType teamType) {
        this.teamType = teamType;
        this.players = new HashSet<>();
        this.displayItem = ItemBuilders.normal(teamType.getMaterial())
                .title(teamType.getColorCode() + "Team " + teamType.getName())
                .build();
    }
}
