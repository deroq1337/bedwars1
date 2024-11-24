package com.github.lukas2o11.bedwars.game.user;

import com.github.lukas2o11.bedwars.game.teams.BedWarsGameTeam;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface BedWarsUser {

    @NotNull UUID getUUID();

    Optional<BedWarsGameTeam> getGameTeam();

    boolean isAlive();

    void setAlive(boolean alive);

    void giveItems();
}
