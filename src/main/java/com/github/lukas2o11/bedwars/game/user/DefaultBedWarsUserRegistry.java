package com.github.lukas2o11.bedwars.game.user;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class DefaultBedWarsUserRegistry implements BedWarsUserRegistry {

    private @NotNull final BedWarsGame<?> game;
    private @NotNull final Map<UUID, BedWarsUser> userMap = new ConcurrentHashMap<>();

    @Override
    public @NotNull BedWarsUser addUser(@NotNull UUID uuid, boolean alive) {
        final BedWarsUser user = DefaultBedWarsUser.create(game, uuid, alive);
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
