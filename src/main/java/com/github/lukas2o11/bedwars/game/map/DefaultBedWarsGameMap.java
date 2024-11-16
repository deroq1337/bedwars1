package com.github.lukas2o11.bedwars.game.map;

import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsGameMapLocation;
import com.github.lukas2o11.bedwars.game.spawners.BedWarsGameResourceSpawnerType;
import com.github.lukas2o11.bedwars.game.teams.BedWarsGameTeamType;
import lombok.*;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DefaultBedWarsGameMap implements BedWarsGameMap {

    private String name;
    private int teamCount;
    private int teamSize;
    private int minPlayers;

    public DefaultBedWarsGameMap(final String name, final int teamCount, final int teamSize, final int minPlayers) {
        this.name = name;
        this.teamCount = teamCount;
        this.teamSize = teamSize;
        this.minPlayers = minPlayers;
    }

    private ObjectId id;
    private Set<BedWarsGameTeamType> teams;
    private Map<BedWarsGameTeamType, BedWarsDirectedGameMapLocation> teamSpawnLocations;
    private Map<BedWarsGameTeamType, BedWarsGameMapLocation> teamBedLocations;
    private Map<Integer, BedWarsDirectedGameMapLocation> shopLocations;
    private Map<BedWarsGameResourceSpawnerType, Map<Integer, BedWarsGameMapLocation>> spawnerLocations;
    private BedWarsDirectedGameMapLocation respawnLocation;
    private BedWarsDirectedGameMapLocation spectatorLocation;
    private Set<Material> breakableBlocks;
    private Material displayItem;

    @Override
    public boolean addTeam(@NotNull BedWarsGameTeamType teamType) {
        if (teams == null) {
            this.teams = new HashSet<>();
        }
        return teams.add(teamType);
    }

    @Override
    public boolean removeTeam(@NotNull BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teams)
                .map(teams -> teams.remove(teamType))
                .orElse(false);
    }

    @Override
    public boolean hasTeam(@NotNull BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teams)
                .map(teams -> teams.contains(teamType))
                .orElse(false);
    }

    @Override
    public void addTeamSpawnLocation(@NotNull BedWarsGameTeamType teamType, @NotNull BedWarsDirectedGameMapLocation location) {
        if (teamSpawnLocations == null) {
            this.teamSpawnLocations = new HashMap<>();
        }
        teamSpawnLocations.put(teamType, location);
    }

    @Override
    public boolean removeTeamSpawnLocation(@NotNull BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teamSpawnLocations)
                .map(teamSpawnLocations -> teamSpawnLocations.remove(teamType) != null)
                .orElse(false);
    }

    @Override
    public void addTeamBedLocation(@NotNull BedWarsGameTeamType teamType, @NotNull BedWarsGameMapLocation location) {
        if (teamBedLocations == null) {
            this.teamBedLocations = new HashMap<>();
        }
        teamBedLocations.put(teamType, location);
    }

    @Override
    public boolean removeTeamBedLocation(@NotNull BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teamBedLocations)
                .map(teamBedLocations -> teamBedLocations.remove(teamType) != null)
                .orElse(false);
    }

    @Override
    public int addShopLocation(@NotNull BedWarsDirectedGameMapLocation location) {
        if (shopLocations == null) {
            this.shopLocations = new HashMap<>();
        }

        final int id = shopLocations.size() + 1;
        shopLocations.put(id, location);
        return id;
    }

    @Override
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

    @Override
    public void addSpawnerLocation(@NotNull BedWarsGameResourceSpawnerType spawnerType, @NotNull BedWarsDirectedGameMapLocation location) {
        if (spawnerLocations == null) {
            this.spawnerLocations = new HashMap<>();
        }

        final Map<Integer, BedWarsGameMapLocation> idLocations = spawnerLocations.computeIfAbsent(spawnerType, o -> new HashMap<>());
        idLocations.put(idLocations.size() + 1, location);
    }

    @Override
    public boolean removeSpawnerLocation(@NotNull BedWarsGameResourceSpawnerType spawnerType, int id) {
        return Optional.ofNullable(spawnerLocations)
                .map(spawnerLocations -> Optional.ofNullable(spawnerLocations.get(spawnerType))
                        .map(idLocations -> idLocations.remove(id) != null)
                        .orElse(false))
                .orElse(false);
    }

    @Override
    public void setRespawnLocation(@NotNull BedWarsDirectedGameMapLocation location) {
        this.respawnLocation = location;
    }

    @Override
    public void setSpectatorLocation(@NotNull BedWarsDirectedGameMapLocation location) {
        this.spectatorLocation = location;
    }

    @Override
    public boolean addBreakableBlock(@NotNull Material material) {
        if (breakableBlocks == null) {
            this.breakableBlocks = new HashSet<>();
        }
        return breakableBlocks.add(material);
    }

    @Override
    public boolean removeBreakableBlock(@NotNull Material material) {
        return Optional.ofNullable(breakableBlocks)
                .map(blocks -> blocks.remove(material))
                .orElse(false);
    }

    @Override
    public Material getDisplayItem() {
        return displayItem;
    }

    @Override
    public void setDisplayItem(@NotNull Material material) {
        this.displayItem = material;
    }
}
