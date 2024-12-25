package com.github.deroq1337.bedwars.data.game.user;

import com.github.deroq1337.bedwars.data.game.teams.BedWarsGameTeam;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class DefaultBedWarsUser implements BedWarsUser {

    private final @NotNull UUID uuid;

    @Getter
    private Optional<BedWarsGameTeam> gameTeam;

    @Getter
    private boolean alive;

    private DefaultBedWarsUser(@NotNull UUID uuid, boolean alive) {
        this.uuid = uuid;
        this.alive = alive;
    }

    public static @NotNull DefaultBedWarsUser create(@NotNull UUID uuid, boolean alive) {
        return new DefaultBedWarsUser(uuid, alive);
    }

    @Override
    public Optional<Player> getBukkitPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }
}
