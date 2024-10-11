package com.github.lukas2o11.bedwars.game.user;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BedWarsUserRegistry {

    void addUser(final UUID uuid, final boolean alive);

    void removeUser(final UUID uuid);

    List<BedWarsUser> listAliveUsers();

    Collection<BedWarsUser> listUsers();
}
