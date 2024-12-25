package com.github.deroq1337.bedwars.data.game.user;

import com.github.deroq1337.bedwars.data.game.teams.BedWarsGameTeam;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface BedWarsUser {

    @NotNull UUID getUuid();

    Optional<BedWarsGameTeam> getGameTeam();

    boolean isAlive();

    void setAlive(boolean alive);

    Optional<Player> getBukkitPlayer();
}
