package com.github.deroq1337.bedwars.data.game.user;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class BedWarsUserRegistry {

    private final @NotNull BedWarsGame game;
    private final @NotNull Map<UUID, BedWarsUser> userMap = new ConcurrentHashMap<>();

    public @NotNull BedWarsUser addUser(@NotNull UUID uuid, boolean alive) {
        final BedWarsUser user = new BedWarsUser(game, uuid, alive);
        userMap.put(uuid, user);
        return user;
    }

    public void removeUser(@NotNull UUID uuid) {
        userMap.remove(uuid);
    }

    public Optional<BedWarsUser> getUser(@NotNull UUID uuid) {
        return Optional.ofNullable(userMap.get(uuid));
    }

    public @NotNull List<BedWarsUser> getAliveUsers() {
        return userMap.values().stream()
                .filter(BedWarsUser::isAlive)
                .toList();
    }

    public @NotNull Collection<BedWarsUser> getUsers() {
        return userMap.values();
    }
}
