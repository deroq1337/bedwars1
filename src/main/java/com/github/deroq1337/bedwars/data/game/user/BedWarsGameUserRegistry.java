package com.github.deroq1337.bedwars.data.game.user;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class BedWarsGameUserRegistry {

    private final @NotNull BedWarsGame game;
    private final @NotNull Map<UUID, BedWarsGameUser> userMap = new ConcurrentHashMap<>();

    public @NotNull BedWarsGameUser addUser(@NotNull UUID uuid, boolean alive) {
        final BedWarsGameUser user = new BedWarsGameUser(game, uuid, alive);
        userMap.put(uuid, user);
        return user;
    }

    public void removeUser(@NotNull UUID uuid) {
        userMap.remove(uuid);
    }

    public Optional<BedWarsGameUser> getUser(@NotNull UUID uuid) {
        return Optional.ofNullable(userMap.get(uuid));
    }

    public @NotNull List<BedWarsGameUser> getAliveUsers() {
        return userMap.values().stream()
                .filter(BedWarsGameUser::isAlive)
                .toList();
    }

    public @NotNull Collection<BedWarsGameUser> getUsers() {
        return userMap.values();
    }
}
