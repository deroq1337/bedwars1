package com.github.lukas2o11.bedwars.game.map;

import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsGameMapLocation;
import com.github.lukas2o11.bedwars.game.spawners.BedWarsGameResourceSpawnerType;
import com.github.lukas2o11.bedwars.game.teams.BedWarsGameTeamType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BedWarsGameMap {

    private String name;
    private int minPlayers;
    private int teamCount;
    private int teamSize;
    private Map<BedWarsGameTeamType, BedWarsDirectedGameMapLocation> teamSpawnLocations;
    private Map<BedWarsGameTeamType, BedWarsGameMapLocation> teamBedLocations;
    private List<BedWarsDirectedGameMapLocation> shopLocations;
    private Map<BedWarsGameResourceSpawnerType, List<BedWarsGameMapLocation>> spawnerLocations;
    private BedWarsDirectedGameMapLocation respawnLocation;
    private BedWarsDirectedGameMapLocation spectatorLocation;
    private List<Material> breakableBlocks;
}
