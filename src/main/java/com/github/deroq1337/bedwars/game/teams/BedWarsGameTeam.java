package com.github.deroq1337.bedwars.game.teams;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface BedWarsGameTeam {

    @NotNull BedWarsGameTeamType getTeamType();

    boolean isBedDestroyed();

    void setBedDestroyed(boolean destroyed);

    boolean isEliminated();

    void setEliminated(boolean eliminated);

    @NotNull ItemStack getDisplayItem();
}
