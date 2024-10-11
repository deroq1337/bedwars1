package com.github.lukas2o11.bedwars.game.map;

import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsGameMapLocation;
import com.github.lukas2o11.bedwars.game.spawners.BedWarsGameResourceSpawnerType;
import com.github.lukas2o11.bedwars.game.teams.BedWarsGameTeamType;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Map;
import java.util.Set;

public interface BedWarsGameMap {

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

    void addTeamSpawnLocation(final BedWarsGameTeamType teamType, final Location location);

    boolean removeTeamSpawnLocation(final BedWarsGameTeamType teamType);

    Map<BedWarsGameTeamType, BedWarsGameMapLocation> getTeamBedLocations();

    void addTeamBedLocation(final BedWarsGameTeamType teamType, final Location location);

    boolean removeTeamBedLocation(final BedWarsGameTeamType teamType);

    Map<Integer, BedWarsDirectedGameMapLocation> getShopLocations();

    void addShopLocation(final Location location);

    boolean removeShopLocation(final int id);

    Map<BedWarsGameResourceSpawnerType, Map<Integer, BedWarsGameMapLocation>> getSpawnerLocations();

    void addSpawnerLocation(final BedWarsGameResourceSpawnerType spawnerType, final Location location);

    boolean removeSpawnerLocation(final BedWarsGameResourceSpawnerType spawnerType, final int id);

    BedWarsDirectedGameMapLocation getRespawnLocation();

    void setRespawnLocation(final Location location);

    BedWarsDirectedGameMapLocation getSpectatorLocation();

    void setSpectatorLocation(final Location location);

    Set<Material> getBreakableBlocks();

    boolean addBreakableBlock(final Material material);

    boolean removeBreakableBlock(final Material material);
}
