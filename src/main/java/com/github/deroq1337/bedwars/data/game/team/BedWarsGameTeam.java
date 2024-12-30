package com.github.deroq1337.bedwars.data.game.team;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import lombok.*;
import org.bukkit.Location;
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
public class BedWarsGameTeam {

    private final @NotNull BedWarsGame game;
    private final @NotNull BedWarsGameTeamType teamType;
    private Optional<Location> spawnLocation = Optional.empty();
    private Optional<Location> bedLocation = Optional.empty();
    private boolean bedDestroyed;
    private boolean eliminated;

    public @NotNull ItemStack getDisplayItem(@NotNull BedWarsGameUser user) {
        List<BedWarsGameUser> users = getUsers();
        return ItemBuilders.normal(teamType.getMaterial())
                .title(user.getMessage("team_selector_inventory_item_name",
                        getNameWithColor(user), users.size(), game.getMainConfig().getTeamSize()))
                .lore(getUsernames(users).stream()
                        .map(name -> user.getMessage("team_selector_inventory_item_lore", getColor(user) + name))
                        .toList()
                        .toArray(new String[0]))
                .build();
    }

    public void teleport(@NotNull BedWarsGameUser user) {
        spawnLocation.ifPresent(location -> {
            user.getBukkitPlayer().ifPresent(player -> player.teleport(location));
        });
    }

    public void announce(@NotNull BedWarsGameUser user) {
        user.sendMessage("team_announcement", getNameWithColor(user));
    }

    public @NotNull List<BedWarsGameUser> getUsers() {
        return game.getUserRegistry().getAliveUsers().stream()
                .filter(u -> u.getTeam()
                        .map(team -> team.equals(this))
                        .orElse(false))
                .toList();
    }

    private @NotNull List<String> getUsernames(@NotNull List<BedWarsGameUser> users) {
        return users.stream()
                .map(u -> u.getBukkitPlayer()
                        .map(Player::getName)
                        .orElse("N/A"))
                .toList();
    }

    public @NotNull String getColor(@NotNull BedWarsGameUser user) {
        return user.getMessage(teamType.getColorCode());
    }

    public @NotNull String getName(@NotNull BedWarsGameUser user) {
        return user.getMessage(teamType.getName());
    }

    public @NotNull String getNameWithColor(@NotNull BedWarsGameUser user) {
        return getColor(user) + getName(user);
    }
}
