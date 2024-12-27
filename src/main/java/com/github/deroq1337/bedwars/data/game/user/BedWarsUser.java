package com.github.deroq1337.bedwars.data.game.user;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class BedWarsUser {

    private final @NotNull UUID uuid;

    @Getter
    private boolean alive;

    private BedWarsUser(@NotNull UUID uuid, boolean alive) {
        this.uuid = uuid;
        this.alive = alive;
    }

    public static @NotNull BedWarsUser create(@NotNull UUID uuid, boolean alive) {
        return new BedWarsUser(uuid, alive);
    }

    public void sendMessage(@NotNull String message) {
        getBukkitPlayer().ifPresent(player -> player.sendMessage(message));
    }

    public Optional<Player> getBukkitPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }
}
