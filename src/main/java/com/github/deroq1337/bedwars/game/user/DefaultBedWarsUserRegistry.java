package com.github.deroq1337.bedwars.game.user;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class DefaultBedWarsUserRegistry implements BedWarsUserRegistry {

    private final @NotNull Map<UUID, BedWarsUser> userMap = new ConcurrentHashMap<>();

    @Override
    public @NotNull BedWarsUser addUser(@NotNull UUID uuid, boolean alive) {
        final BedWarsUser user = DefaultBedWarsUser.create(uuid, alive);
        userMap.put(uuid, user);
        return user;
    }

    @Override
    public void removeUser(@NotNull UUID uuid) {
        userMap.remove(uuid);
    }

    @Override
    public @NotNull List<BedWarsUser> getAliveUsers() {
        return userMap.values().stream()
                .filter(BedWarsUser::isAlive)
                .toList();
    }

    @Override
    public @NotNull Collection<BedWarsUser> getUsers() {
        return userMap.values();
    }
}
