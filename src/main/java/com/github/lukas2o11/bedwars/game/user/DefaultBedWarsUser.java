package com.github.lukas2o11.bedwars.game.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DefaultBedWarsUser implements BedWarsUser {

    private UUID uuid;
    private boolean alive;

    private DefaultBedWarsUser(final UUID uuid, final boolean alive) {
        this.uuid = uuid;
        this.alive = alive;
    }

    public static DefaultBedWarsUser create(final UUID uuid, final boolean alive) {
        return new DefaultBedWarsUser(uuid, alive);
    }
}
