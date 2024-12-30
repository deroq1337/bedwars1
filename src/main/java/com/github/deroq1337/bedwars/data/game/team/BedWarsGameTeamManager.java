package com.github.deroq1337.bedwars.data.game.team;

import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface BedWarsGameTeamManager {

    void fillTeams();

    void initLocations();

    boolean handleInventoryClick(@NotNull BedWarsGameUser user, @NotNull InventoryClickEvent event);

    @NotNull List<BedWarsGameTeam> getTeams();

    Optional<BedWarsGameTeam> getTeamByItem(@NotNull BedWarsGameUser user, @NotNull ItemStack item);

    @NotNull ItemStack getItem(@NotNull BedWarsGameUser user);

    @NotNull Inventory getInventory(@NotNull BedWarsGameUser user);
}
