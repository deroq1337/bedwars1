package com.github.lukas2o11.bedwars.game.user;

import java.util.UUID;

public interface BedWarsUser {

    UUID getUuid();

    boolean isAlive();

    void setAlive(final boolean alive);
}
