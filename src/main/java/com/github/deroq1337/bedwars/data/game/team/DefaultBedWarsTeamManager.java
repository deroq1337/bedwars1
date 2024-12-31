package com.github.deroq1337.bedwars.data.game.team;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.config.MainConfig;
import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsMapDirectedLocation;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsMapLocation;
import com.github.deroq1337.bedwars.data.game.state.BedWarsLobbyState;
import com.github.deroq1337.bedwars.data.game.user.BedWarsUser;
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
public class DefaultBedWarsTeamManager implements BedWarsTeamManager {

    private final @NotNull BedWarsGame game;
    private final @NotNull MainConfig mainConfig;
    private final @NotNull List<BedWarsTeam> teams;

    public DefaultBedWarsTeamManager(@NotNull BedWarsGame game) {
        this.game = game;
        this.mainConfig = game.getMainConfig();
        this.teams = game.getMainConfig().getTeams()
                .stream().map(teamType -> new BedWarsTeam(game, teamType))
                .toList();
    }

    @Override
    public void fillTeams() {
        Iterator<BedWarsUser> usersWithoutTeam = game.getUserRegistry().getAliveUsers().stream()
                .filter(user -> user.getTeam().isEmpty())
                .toList().iterator();
        int teamIndex = teams.indexOf(teams.stream()
                .min(Comparator.comparingInt(team -> team.getUsers().size()))
                .orElse(teams.getLast()));
        
        while (usersWithoutTeam.hasNext()) {
            BedWarsTeam team = teams.get(teamIndex);
            if (team.getUsers().size() >= mainConfig.getTeamSize()) {
                teamIndex = updateIndex(teamIndex);
                continue;
            }

            usersWithoutTeam.next().setTeam(Optional.of(team));
            teamIndex = updateIndex(teamIndex);
        }
    }

    private int updateIndex(int index) {
        if (index == teams.size() - 1) {
            return 0;
        }

       return index + 1;
    }

    @Override
    public void initLocations() {
        game.getCurrentMap().ifPresent(map -> {
            teams.forEach(team -> {
                team.setSpawnLocation(map.getTeamSpawnLocation(team.getTeamType())
                        .map(BedWarsMapDirectedLocation::toBukkitLocation));
                team.setBedLocation(map.getTeamBedLocation(team.getTeamType())
                        .map(BedWarsMapLocation::toBukkitLocation));
            });
        });
    }

    @Override
    public void destroyBeds(@NotNull List<BedWarsTeam> teams) {
        teams.stream()
                .filter(team -> team.getUsers().isEmpty())
                .forEach(BedWarsTeam::destroyBed);
    }

    @Override
    public boolean handleInventoryClick(@NotNull BedWarsUser user, @NotNull InventoryClickEvent event) {
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
    public Optional<BedWarsTeam> getTeamByType(@NotNull BedWarsTeamType teamType) {
        return teams.stream()
                .filter(team -> team.getTeamType() == teamType)
                .findFirst();
    }

    @Override
    public Optional<BedWarsTeam> getTeamByItem(@NotNull BedWarsUser user, @NotNull ItemStack item) {
        return teams.stream()
                .filter(team -> team.getDisplayItem(user).getType() == item.getType())
                .findFirst();
    }

    @Override
    public @NotNull ItemStack getItem(@NotNull BedWarsUser user) {
        return ItemBuilders.normal(Material.RED_BED)
                .title(user.getMessage("team_selector_item_name"))
                .build();
    }

    @Override
    public @NotNull Inventory getInventory(@NotNull BedWarsUser user) {
        Inventory inventory = Bukkit.createInventory(null, 9, getInventoryTitle(user));
        IntStream.range(0, mainConfig.getTeamCount()).forEach(i -> inventory.setItem(mainConfig.getTeamSlots().get(i), teams.get(i).getDisplayItem(user)));
        return inventory;
    }

    private @NotNull String getInventoryTitle(@NotNull BedWarsUser user) {
        return user.getMessage("team_selector_inventory_title");
    }
}
