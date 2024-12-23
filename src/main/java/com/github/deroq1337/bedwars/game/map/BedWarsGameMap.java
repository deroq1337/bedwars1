package com.github.deroq1337.bedwars.game.map;

import com.github.deroq1337.bedwars.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.deroq1337.bedwars.game.map.serialization.BedWarsGameMapLocation;
import com.github.deroq1337.bedwars.game.spawners.BedWarsGameResourceSpawnerType;
import com.github.deroq1337.bedwars.game.teams.BedWarsGameTeamType;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public interface BedWarsGameMap {

    @NotNull ObjectId getId();

    @NotNull String getName();

    void setName(@NotNull String name);

    int getTeamCount();

    void setTeamCount(int teamCount);

    int getTeamSize();

    void setTeamSize(int teamSize);

    @Nullable Set<BedWarsGameTeamType> getTeams();

    boolean addTeam(@NotNull BedWarsGameTeamType teamType);

    boolean removeTeam(@NotNull BedWarsGameTeamType teamType);

    boolean hasTeam(@NotNull BedWarsGameTeamType teamType);

    @Nullable Map<BedWarsGameTeamType, BedWarsDirectedGameMapLocation> getTeamSpawnLocations();

    void addTeamSpawnLocation(@NotNull BedWarsGameTeamType teamType, @NotNull BedWarsDirectedGameMapLocation location);

    boolean removeTeamSpawnLocation(@NotNull BedWarsGameTeamType teamType);

    @Nullable Map<BedWarsGameTeamType, BedWarsGameMapLocation> getTeamBedLocations();

    void addTeamBedLocation(@NotNull BedWarsGameTeamType teamType, @NotNull BedWarsGameMapLocation location);

    boolean removeTeamBedLocation(@NotNull BedWarsGameTeamType teamType);

    @Nullable Map<Integer, BedWarsDirectedGameMapLocation> getShopLocations();

    int addShopLocation(@NotNull BedWarsDirectedGameMapLocation location);

    boolean removeShopLocation(int id);

    @Nullable Map<BedWarsGameResourceSpawnerType, Map<Integer, BedWarsGameMapLocation>> getSpawnerLocations();

    void addSpawnerLocation(@NotNull BedWarsGameResourceSpawnerType spawnerType, @NotNull BedWarsDirectedGameMapLocation location);

    boolean removeSpawnerLocation(@NotNull BedWarsGameResourceSpawnerType spawnerType, int id);

    @Nullable BedWarsDirectedGameMapLocation getRespawnLocation();

    void setRespawnLocation(@NotNull BedWarsDirectedGameMapLocation location);

    @Nullable BedWarsDirectedGameMapLocation getSpectatorLocation();

    void setSpectatorLocation(@NotNull BedWarsDirectedGameMapLocation location);

    @Nullable Set<Material> getBreakableBlocks();

    boolean addBreakableBlock(@NotNull Material material);

    boolean removeBreakableBlock(@NotNull Material material);

    @Nullable Material getDisplayItem();

    void setDisplayItem(@NotNull Material material);
}
