package com.github.deroq1337.bedwars.data.game.team;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
import lombok.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BedWarsTeam {

    private final @NotNull BedWarsGame game;
    private final @NotNull BedWarsTeamType teamType;
    private Optional<Location> spawnLocation = Optional.empty();
    private Optional<Location> bedLocation = Optional.empty();
    private boolean bedDestroyed;
    private boolean eliminated;

    public @NotNull ItemStack getDisplayItem(@NotNull BedWarsUser user) {
        List<BedWarsUser> users = getUsers();
        return ItemBuilders.normal(teamType.getMaterial())
                .title(user.getMessage("team_selector_inventory_item_name",
                        getNameWithColor(user), users.size(), game.getMainConfig().getTeamSize()))
                .lore(getUsernames(users).stream()
                        .map(name -> user.getMessage("team_selector_inventory_item_lore", getColor(user) + name))
                        .toList()
                        .toArray(new String[0]))
                .build();
    }

    public void teleport(@NotNull BedWarsUser user) {
        spawnLocation.ifPresent(location -> {
            user.getBukkitPlayer().ifPresent(player -> player.teleport(location));
        });
    }

    public void destroyBed() {
        bedLocation.ifPresent(location -> {
            if (Tag.BEDS.isTagged(location.getBlock().getType())) {
                location.getBlock().setType(Material.AIR);
            }
        });
        this.bedDestroyed = true;
    }

    public void announce(@NotNull BedWarsUser user) {
        user.sendMessage("team_announcement", getNameWithColor(user));
    }

    public @NotNull List<BedWarsUser> getUsers() {
        return game.getUserRegistry().getUsers().stream()
                .filter(u -> u.getTeam()
                        .map(team -> team.equals(this))
                        .orElse(false))
                .toList();
    }

    public @NotNull List<BedWarsUser> getAliveUsers() {
        return game.getUserRegistry().getAliveUsers().stream()
                .filter(u -> u.getTeam()
                        .map(team -> team.equals(this))
                        .orElse(false))
                .toList();
    }

    private @NotNull List<String> getUsernames(@NotNull List<BedWarsUser> users) {
        return users.stream()
                .map(u -> u.getBukkitPlayer()
                        .map(Player::getName)
                        .orElse("N/A"))
                .toList();
    }

    public @NotNull String getColor(@NotNull BedWarsUser user) {
        return user.getMessage(teamType.getColorCode());
    }

    public @NotNull String getName(@NotNull BedWarsUser user) {
        return user.getMessage(teamType.getName());
    }

    public @NotNull String getNameWithColor(@NotNull BedWarsUser user) {
        return getColor(user) + getName(user);
    }
}
