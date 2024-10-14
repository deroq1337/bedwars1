package com.github.lukas2o11.bedwars.game.map;

import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsGameMapLocation;
import com.github.lukas2o11.bedwars.game.spawners.BedWarsGameResourceSpawnerType;
import com.github.lukas2o11.bedwars.game.teams.BedWarsGameTeamType;
import org.bson.types.ObjectId;
import org.bukkit.Material;

import java.util.Map;
import java.util.Set;

public interface BedWarsGameMap {

    ObjectId getId();

    String getName();

    void setName(final String name);

    int getTeamCount();

    void setTeamCount(final int teamCount);

    int getTeamSize();

    void setTeamSize(final int teamSize);

    Set<BedWarsGameTeamType> getTeams();

    boolean addTeam(final BedWarsGameTeamType teamType);

    boolean removeTeam(final BedWarsGameTeamType teamType);

    Map<BedWarsGameTeamType, BedWarsDirectedGameMapLocation> getTeamSpawnLocations();

    void addTeamSpawnLocation(final BedWarsGameTeamType teamType, final BedWarsDirectedGameMapLocation location);

    boolean removeTeamSpawnLocation(final BedWarsGameTeamType teamType);

    Map<BedWarsGameTeamType, BedWarsGameMapLocation> getTeamBedLocations();

    void addTeamBedLocation(final BedWarsGameTeamType teamType, final BedWarsDirectedGameMapLocation location);

    boolean removeTeamBedLocation(final BedWarsGameTeamType teamType);

    Map<Integer, BedWarsDirectedGameMapLocation> getShopLocations();

    void addShopLocation(final BedWarsDirectedGameMapLocation location);

    boolean removeShopLocation(final int id);

    Map<BedWarsGameResourceSpawnerType, Map<Integer, BedWarsGameMapLocation>> getSpawnerLocations();

    void addSpawnerLocation(final BedWarsGameResourceSpawnerType spawnerType, final BedWarsDirectedGameMapLocation location);

    boolean removeSpawnerLocation(final BedWarsGameResourceSpawnerType spawnerType, final int id);

    BedWarsDirectedGameMapLocation getRespawnLocation();

    void setRespawnLocation(final BedWarsDirectedGameMapLocation location);

    BedWarsDirectedGameMapLocation getSpectatorLocation();

    void setSpectatorLocation(final BedWarsDirectedGameMapLocation location);

    Set<Material> getBreakableBlocks();

    boolean addBreakableBlock(final Material material);

    boolean removeBreakableBlock(final Material material);
}
