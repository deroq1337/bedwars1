package com.github.lukas2o11.bedwars.game.teams;

import com.github.lukas2o11.bedwars.game.item.ItemBuilders;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class DefaultBedWarsGameTeam implements BedWarsGameTeam {

    private final BedWarsGameTeamType teamType;
    private final Set<UUID> players;
    private final ItemStack displayItem;
    private boolean bedDestroyed = false;
    private boolean eliminated = false;

    public DefaultBedWarsGameTeam(BedWarsGameTeamType teamType) {
        this.teamType = teamType;
        this.players = new HashSet<>();
        this.displayItem = ItemBuilders.normal(teamType.getMaterial())
                .title(teamType.getColorCode() + "Team " + teamType.getName())
                .build();
    }
}
