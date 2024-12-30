package com.github.deroq1337.bedwars.data.game.team;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.config.MainConfig;
import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsGameMapLocation;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsGameUser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Getter
public class DefaultBedWarsGameTeamManager implements BedWarsGameTeamManager {

    private final @NotNull BedWarsGame game;
    private final @NotNull MainConfig mainConfig;
    private final @NotNull List<BedWarsGameTeam> teams;

    public DefaultBedWarsGameTeamManager(@NotNull BedWarsGame game) {
        this.game = game;
        this.mainConfig = game.getMainConfig();
        this.teams = game.getMainConfig().getTeams()
                .stream().map(teamType -> new BedWarsGameTeam(game, teamType))
                .toList();
    }

    @Override
    public void fillTeams() {
        Iterator<BedWarsGameUser> usersWithoutTeam = game.getUserRegistry().getAliveUsers().stream()
                .filter(user -> user.getTeam().isEmpty())
                .toList().iterator();
        int teamIndex = teams.indexOf(teams.stream()
                .min(Comparator.comparingInt(team -> team.getUsers().size()))
                .orElse(teams.getLast()));
        
        while (usersWithoutTeam.hasNext()) {
            BedWarsGameUser user = usersWithoutTeam.next();
            user.setTeam(Optional.of(teams.get(teamIndex)));

            if (teamIndex == teams.size() - 1) {
                teamIndex = 0;
                continue;
            }

            teamIndex++;
        }
    }

    @Override
    public void initLocations() {
        game.getGameMap().ifPresent(gameMap -> {
            teams.forEach(team -> {
                team.setSpawnLocation(gameMap.getTeamSpawnLocation(team.getTeamType())
                        .map(BedWarsDirectedGameMapLocation::toBukkitLocation));
                team.setBedLocation(gameMap.getTeamBedLocation(team.getTeamType())
                        .map(BedWarsGameMapLocation::toBukkitLocation));
            });
        });
    }

    @Override
    public boolean handleInventoryClick(@NotNull BedWarsGameUser user, @NotNull InventoryClickEvent event) {
        return game.getGameState().map(gameState -> {
            if (!event.getView().getTitle().equals(getInventoryTitle(user))) {
                return false;
            }

            event.setCancelled(true);
            if (!(gameState instanceof BedWarsLobbyState)) {
                return true;
            }

            Optional.ofNullable(event.getCurrentItem()).flatMap(item -> {
                return getTeamByItem(user, item).map(team -> {
                    boolean sameTeam = user.getTeam()
                            .map(currentTeam -> currentTeam.equals(team))
                            .orElse(false);
                    if (sameTeam) {
                        user.sendMessage("already_in_team");
                        return true;
                    }

                    if (team.getUsers().size() > mainConfig.getTeamSize()) {
                        user.sendMessage("team_full");
                        return true;
                    }

                    user.setTeam(Optional.of(team));
                    user.sendMessage("team_joined", team.getNameWithColor(user));
                    event.getWhoClicked().closeInventory();
                    return true;
                });
            });
            return true;
        }).orElse(false);
    }

    @Override
    public Optional<BedWarsGameTeam> getTeamByItem(@NotNull BedWarsGameUser user, @NotNull ItemStack item) {
        return teams.stream()
                .filter(team -> team.getDisplayItem(user).getType() == item.getType())
                .findFirst();
    }

    @Override
    public @NotNull ItemStack getItem(@NotNull BedWarsGameUser user) {
        return ItemBuilders.normal(Material.RED_BED)
                .title(user.getMessage("team_selector_item_name"))
                .build();
    }

    @Override
    public @NotNull Inventory getInventory(@NotNull BedWarsGameUser user) {
        Inventory inventory = Bukkit.createInventory(null, 9, getInventoryTitle(user));
        IntStream.range(0, mainConfig.getTeamCount()).forEach(i -> inventory.setItem(mainConfig.getTeamSlots().get(i), teams.get(i).getDisplayItem(user)));
        return inventory;
    }

    private @NotNull String getInventoryTitle(@NotNull BedWarsGameUser user) {
        return user.getMessage("team_selector_inventory_title");
    }
}
