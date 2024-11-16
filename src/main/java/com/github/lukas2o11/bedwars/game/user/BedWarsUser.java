package com.github.lukas2o11.bedwars.game.user;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface BedWarsUser {

    @NotNull UUID getUUID();

    boolean isAlive();

    void setAlive(boolean alive);

    void giveItems();
}
