package com.github.deroq1337.bedwars.data.game.team;

import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface BedWarsTeamManager {

    void fillTeams();

    void initLocations();

    void destroyBeds(@NotNull List<BedWarsTeam> teams);

    boolean handleInventoryClick(@NotNull BedWarsUser user, @NotNull InventoryClickEvent event);

    @NotNull List<BedWarsTeam> getTeams();

    Optional<BedWarsTeam> getTeamByType(@NotNull BedWarsTeamType teamType);

    Optional<BedWarsTeam> getTeamByItem(@NotNull BedWarsUser user, @NotNull ItemStack item);

    @NotNull ItemStack getItem(@NotNull BedWarsUser user);

    @NotNull Inventory getInventory(@NotNull BedWarsUser user);
}
