package com.github.lukas2o11.bedwars.game.user;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {

    private UUID uuid;
    private boolean alive;

    private User(final UUID uuid, final boolean alive) {
        this.uuid = uuid;
        this.alive = alive;
    }

    public static User create(final UUID uuid, final boolean alive) {
        return new User(uuid, alive);
    }
}
