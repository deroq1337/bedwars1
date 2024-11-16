package com.github.lukas2o11.bedwars.game.map;

import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsGameMapLocation;
import com.github.lukas2o11.bedwars.game.spawners.BedWarsGameResourceSpawnerType;
import com.github.lukas2o11.bedwars.game.teams.BedWarsGameTeamType;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public interface BedWarsGameMap {

    ObjectId getId();

    String getName();

    void setName(@NotNull String name);

    int getTeamCount();

    void setTeamCount(int teamCount);

    int getTeamSize();

    void setTeamSize(int teamSize);

    Set<BedWarsGameTeamType> getTeams();

    boolean addTeam(@NotNull BedWarsGameTeamType teamType);

    boolean removeTeam(@NotNull BedWarsGameTeamType teamType);

    boolean hasTeam(@NotNull BedWarsGameTeamType teamType);

    Map<BedWarsGameTeamType, BedWarsDirectedGameMapLocation> getTeamSpawnLocations();

    void addTeamSpawnLocation(@NotNull BedWarsGameTeamType teamType, @NotNull BedWarsDirectedGameMapLocation location);

    boolean removeTeamSpawnLocation(@NotNull BedWarsGameTeamType teamType);

    Map<BedWarsGameTeamType, BedWarsGameMapLocation> getTeamBedLocations();

    void addTeamBedLocation(@NotNull BedWarsGameTeamType teamType, @NotNull BedWarsGameMapLocation location);

    boolean removeTeamBedLocation(@NotNull BedWarsGameTeamType teamType);

    Map<Integer, BedWarsDirectedGameMapLocation> getShopLocations();

    int addShopLocation(@NotNull BedWarsDirectedGameMapLocation location);

    boolean removeShopLocation(int id);

    Map<BedWarsGameResourceSpawnerType, Map<Integer, BedWarsGameMapLocation>> getSpawnerLocations();

    void addSpawnerLocation(@NotNull BedWarsGameResourceSpawnerType spawnerType, @NotNull BedWarsDirectedGameMapLocation location);

    boolean removeSpawnerLocation(@NotNull BedWarsGameResourceSpawnerType spawnerType, int id);

    BedWarsDirectedGameMapLocation getRespawnLocation();

    void setRespawnLocation(@NotNull BedWarsDirectedGameMapLocation location);

    BedWarsDirectedGameMapLocation getSpectatorLocation();

    void setSpectatorLocation(@NotNull BedWarsDirectedGameMapLocation location);

    Set<Material> getBreakableBlocks();

    boolean addBreakableBlock(@NotNull Material material);

    boolean removeBreakableBlock(@NotNull Material material);

    Material getDisplayItem();

    void setDisplayItem(@NotNull Material material);
}
