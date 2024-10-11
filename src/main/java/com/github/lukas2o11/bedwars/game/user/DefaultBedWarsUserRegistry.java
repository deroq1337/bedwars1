package com.github.lukas2o11.bedwars.game.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBedWarsUserRegistry implements BedWarsUserRegistry {

    private final Map<UUID, BedWarsUser> userMap = new ConcurrentHashMap<>();

    @Override
    public void addUser(final UUID uuid, final boolean alive) {
        userMap.put(uuid, DefaultBedWarsUser.create(uuid, alive));
    }

    @Override
    public void removeUser(final UUID uuid) {
        userMap.remove(uuid);
    }

    @Override
    public List<BedWarsUser> listAliveUsers() {
        return userMap.values().stream()
                .filter(BedWarsUser::isAlive)
                .toList();
    }

    @Override
    public Collection<BedWarsUser> listUsers() {
        return userMap.values();
    }
}
