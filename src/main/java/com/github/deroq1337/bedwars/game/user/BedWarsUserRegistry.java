package com.github.deroq1337.bedwars.game.user;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BedWarsUserRegistry {

    @NotNull BedWarsUser addUser(@NotNull UUID uuid, boolean alive);

    void removeUser(@NotNull UUID uuid);

    @NotNull List<BedWarsUser> getAliveUsers();

    @NotNull Collection<BedWarsUser> getUsers();
}
