package com.github.lukas2o11.bedwars.game.teams;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface BedWarsGameTeamManager {

    @NotNull List<BedWarsGameTeam> getTeams();

    void addTeam(BedWarsGameTeam team);

    default @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, "§8Teamauswahl");
        AtomicInteger slot = new AtomicInteger(0);
        getTeams().forEach(team -> inventory.setItem(slot.getAndIncrement(), team.getDisplayItem()));
        return inventory;
    }
}
