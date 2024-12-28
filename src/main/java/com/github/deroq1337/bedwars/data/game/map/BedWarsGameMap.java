package com.github.deroq1337.bedwars.data.game.map;

import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsGameMapLocation;
import com.github.deroq1337.bedwars.data.game.spawners.BedWarsGameResourceSpawnerType;
import com.github.deroq1337.bedwars.data.game.team.BedWarsGameTeamType;
import lombok.*;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BedWarsGameMap {

    private @NotNull ObjectId id;
    private @NotNull String name;
    private @Nullable Set<BedWarsGameTeamType> teams;
    private @Nullable Map<BedWarsGameTeamType, BedWarsDirectedGameMapLocation> teamSpawnLocations;
    private @Nullable Map<BedWarsGameTeamType, BedWarsGameMapLocation> teamBedLocations;
    private @Nullable Map<Integer, BedWarsDirectedGameMapLocation> shopLocations;
    private @Nullable Map<BedWarsGameResourceSpawnerType, Map<Integer, BedWarsGameMapLocation>> spawnerLocations;
    private @Nullable BedWarsDirectedGameMapLocation respawnLocation;
    private @Nullable BedWarsDirectedGameMapLocation spectatorLocation;
    private @Nullable Set<Material> breakableBlocks;
    private @Nullable Material displayItem;

    private BedWarsGameMap(@NotNull String name) {
        this.name = name;
    }

    public static @NotNull BedWarsGameMap create(@NotNull String name) {
        return new BedWarsGameMap(name);
    }

    public boolean addTeam(@NotNull BedWarsGameTeamType teamType) {
        if (teams == null) {
            this.teams = new HashSet<>();
        }
        return teams.add(teamType);
    }

    public boolean removeTeam(@NotNull BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teams)
                .map(teams -> teams.remove(teamType))
                .orElse(false);
    }

    public boolean hasTeam(@NotNull BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teams)
                .map(teams -> teams.contains(teamType))
                .orElse(false);
    }

    public int getTeamCount() {
        return Optional.ofNullable(teams)
                .map(Set::size)
                .orElse(0);
    }

    public void addTeamSpawnLocation(@NotNull BedWarsGameTeamType teamType, @NotNull BedWarsDirectedGameMapLocation location) {
        if (teamSpawnLocations == null) {
            this.teamSpawnLocations = new HashMap<>();
        }
        teamSpawnLocations.put(teamType, location);
    }

    public boolean removeTeamSpawnLocation(@NotNull BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teamSpawnLocations)
                .map(teamSpawnLocations -> teamSpawnLocations.remove(teamType) != null)
                .orElse(false);
    }

    public void addTeamBedLocation(@NotNull BedWarsGameTeamType teamType, @NotNull BedWarsGameMapLocation location) {
        if (teamBedLocations == null) {
            this.teamBedLocations = new HashMap<>();
        }
        teamBedLocations.put(teamType, location);
    }

    public boolean removeTeamBedLocation(@NotNull BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teamBedLocations)
                .map(teamBedLocations -> teamBedLocations.remove(teamType) != null)
                .orElse(false);
    }

    public int addShopLocation(@NotNull BedWarsDirectedGameMapLocation location) {
        if (shopLocations == null) {
            this.shopLocations = new HashMap<>();
        }

        final int id = shopLocations.size() + 1;
        shopLocations.put(id, location);
        return id;
    }

    public boolean removeShopLocation(int id) {
        return removeAndUpdateMap(shopLocations, id);
    }

    public int addSpawnerLocation(@NotNull BedWarsGameResourceSpawnerType spawnerType, @NotNull BedWarsDirectedGameMapLocation location) {
        if (spawnerLocations == null) {
            this.spawnerLocations = new HashMap<>();
        }

        Map<Integer, BedWarsGameMapLocation> idLocations = spawnerLocations.computeIfAbsent(spawnerType, o -> new HashMap<>());
        final int id = idLocations.size() + 1;
        idLocations.put(id, location);
        return id;
    }

    public boolean removeSpawnerLocation(@NotNull BedWarsGameResourceSpawnerType spawnerType, int id) {
        return Optional.ofNullable(spawnerLocations).map(spawnerLocations -> {
            return Optional.ofNullable(spawnerLocations.get(spawnerType)).map(spawnerTypeLocation -> {
                return removeAndUpdateMap(spawnerTypeLocation, id);
            }).orElse(false);
        }).orElse(false);
    }

    private <T> boolean removeAndUpdateMap(@Nullable Map<Integer, T> map, int id) {
        return Optional.ofNullable(map).map(idMap -> {
            boolean removed = idMap.remove(id) != null;
            if (!removed) {
                return false;
            }

            map.keySet().stream()
                    .filter(i -> i > id)
                    .forEach(i -> map.put(i - 1, map.remove(i)));
            return true;
        }).orElse(false);
    }

    public void setRespawnLocation(@NotNull BedWarsDirectedGameMapLocation location) {
        this.respawnLocation = location;
    }

    public void setSpectatorLocation(@NotNull BedWarsDirectedGameMapLocation location) {
        this.spectatorLocation = location;
    }

    public boolean addBreakableBlock(@NotNull Material material) {
        if (breakableBlocks == null) {
            this.breakableBlocks = new HashSet<>();
        }
        return breakableBlocks.add(material);
    }

    public boolean removeBreakableBlock(@NotNull Material material) {
        return Optional.ofNullable(breakableBlocks)
                .map(blocks -> blocks.remove(material))
                .orElse(false);
    }

    public @NotNull Material getDisplayItem() {
        return Optional.ofNullable(displayItem).orElse(Material.BEDROCK);
    }

    public void setDisplayItem(@NotNull Material material) {
        this.displayItem = material;
    }
}
