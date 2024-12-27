package com.github.deroq1337.bedwars.data.game.user;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class BedWarsUser {

    private final @NotNull BedWarsGame game;
    private final @NotNull UUID uuid;
    private boolean alive;
    private @NotNull Locale locale;

    public BedWarsUser(@NotNull BedWarsGame game, @NotNull UUID uuid, boolean alive) {
        this.game = game;
        this.uuid = uuid;
        this.alive = alive;
        this.locale = Locale.forLanguageTag("de-DE");
    }

    public void sendMessage(@NotNull String key, Object... params) {
        getBukkitPlayer().ifPresent(player -> {
            player.sendMessage(getMessage(key, params));
        });
    }

    public @NotNull String getMessage(@NotNull String key, Object... params) {
        return ChatColor.translateAlternateColorCodes('&', MessageFormat.format(game.getBedWars().getLanguageManager().getMessage(locale, key), params));
    }

    public Optional<Player> getBukkitPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }
}
