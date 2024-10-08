package com.github.lukas2o11.bedwars.game.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRegistry {

    private final Map<UUID, User> userMap = new ConcurrentHashMap<>();

    public void addUser(UUID uuid, boolean alive) {
        userMap.put(uuid, User.create(uuid, alive));
    }

    public void removeUser(UUID uuid) {
        userMap.remove(uuid);
    }

    public List<User> listAliveUsers() {
        return userMap.values().stream()
                .filter(User::isAlive)
                .toList();
    }

    public Collection<User> listUsers() {
        return userMap.values();
    }
}
