package com.github.deroq1337.bedwars.data.game.map;

import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsGameMapLocation;
import com.github.deroq1337.bedwars.data.game.spawners.BedWarsGameResourceSpawnerType;
import com.github.deroq1337.bedwars.data.game.teams.BedWarsGameTeamType;
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
    private int teamCount;
    private int teamSize;
    private int minPlayers;
    private @Nullable Set<BedWarsGameTeamType> teams;
    private @Nullable Map<BedWarsGameTeamType, BedWarsDirectedGameMapLocation> teamSpawnLocations;
    private @Nullable Map<BedWarsGameTeamType, BedWarsGameMapLocation> teamBedLocations;
    private @Nullable Map<Integer, BedWarsDirectedGameMapLocation> shopLocations;
    private @Nullable Map<BedWarsGameResourceSpawnerType, Map<Integer, BedWarsGameMapLocation>> spawnerLocations;
    private @Nullable BedWarsDirectedGameMapLocation respawnLocation;
    private @Nullable BedWarsDirectedGameMapLocation spectatorLocation;
    private @Nullable Set<Material> breakableBlocks;
    private @Nullable Material displayItem;

    public BedWarsGameMap(@NotNull String name, int teamCount, int teamSize, int minPlayers) {
        this.name = name;
        this.teamCount = teamCount;
        this.teamSize = teamSize;
        this.minPlayers = minPlayers;
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
        return Optional.ofNullable(shopLocations)
                .map(locations -> {
                    final boolean removed = locations.remove(id) != null;
                    if (removed) {
                        locations.keySet().stream()
                                .filter(i -> i > id)
                                .forEach(i -> {
                                    final BedWarsDirectedGameMapLocation location = locations.remove(i);
                                    locations.put(i - 1, location);
                                });
                    }
                    return removed;
                }).orElse(false);
    }

    public void addSpawnerLocation(@NotNull BedWarsGameResourceSpawnerType spawnerType, @NotNull BedWarsDirectedGameMapLocation location) {
        if (spawnerLocations == null) {
            this.spawnerLocations = new HashMap<>();
        }

        final Map<Integer, BedWarsGameMapLocation> idLocations = spawnerLocations.computeIfAbsent(spawnerType, o -> new HashMap<>());
        idLocations.put(idLocations.size() + 1, location);
    }

    public boolean removeSpawnerLocation(@NotNull BedWarsGameResourceSpawnerType spawnerType, int id) {
        return Optional.ofNullable(spawnerLocations)
                .map(spawnerLocations -> Optional.ofNullable(spawnerLocations.get(spawnerType))
                        .map(idLocations -> idLocations.remove(id) != null)
                        .orElse(false))
                .orElse(false);
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
