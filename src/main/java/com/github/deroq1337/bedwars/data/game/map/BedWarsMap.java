package com.github.deroq1337.bedwars.data.game.map;

import com.github.deroq1337.bedwars.data.game.map.converters.BedWarsMapDirectedLocationConverter;
import com.github.deroq1337.bedwars.data.game.map.converters.BedWarsMapLocationConverter;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsMapDirectedLocation;
import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsMapLocation;
import com.github.deroq1337.bedwars.data.game.map.converters.EnumConverter;
import com.github.deroq1337.bedwars.data.game.spawners.BedWarsResourceSpawnerType;
import com.github.deroq1337.bedwars.data.game.team.BedWarsTeamType;
import com.github.deroq1337.bedwars.data.game.utils.EnumMapFix;
import lombok.*;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.cubespace.Yamler.Config.InvalidConverterException;
import net.cubespace.Yamler.Config.YamlConfig;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class BedWarsMap extends YamlConfig {

    private @NotNull String name;
    private @Nullable Map<BedWarsTeamType, BedWarsMapDirectedLocation> teamSpawnLocations;
    private @Nullable Map<BedWarsTeamType, BedWarsMapLocation> teamBedLocations;
    private @Nullable Map<Integer, BedWarsMapDirectedLocation> shopLocations;
    private @Nullable Map<BedWarsResourceSpawnerType, Map<Integer, BedWarsMapLocation>> spawnerLocations;
    private @Nullable BedWarsMapDirectedLocation respawnLocation;
    private @Nullable BedWarsMapDirectedLocation spectatorLocation;
    private @Nullable Set<Material> breakableBlocks;
    private @Nullable Material displayItem;

    public BedWarsMap(@NotNull File file) {
        this.CONFIG_FILE = file;

        try {
            addConverter(EnumConverter.class);
            addConverter(BedWarsMapLocationConverter.class);
            addConverter(BedWarsMapDirectedLocationConverter.class);
        } catch (InvalidConverterException e) {
            throw new RuntimeException(e);
        }
    }

    public BedWarsMap(@NotNull File file, boolean load) {
        this(file);
        if (load) {
            try {
                init();
            } catch (InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public BedWarsMap(@NotNull String name) {
        this(new File("plugins/bedwars/maps/" + name.toLowerCase() + ".yml"));
        this.name = name;
    }

    @Override
    public void init() throws InvalidConfigurationException {
        super.init();
        this.teamSpawnLocations = EnumMapFix.fixMapKeys(teamSpawnLocations, BedWarsTeamType.class);
        this.teamBedLocations = EnumMapFix.fixMapKeys(teamBedLocations, BedWarsTeamType.class);
        this.spawnerLocations = EnumMapFix.fixMapKeys(spawnerLocations, BedWarsResourceSpawnerType.class);
    }

    public boolean delete() {
        return CONFIG_FILE.delete();
    }

    public boolean exists() {
        return CONFIG_FILE.exists();
    }

    public void addTeamSpawnLocation(@NotNull BedWarsTeamType teamType, @NotNull BedWarsMapDirectedLocation location) {
        if (teamSpawnLocations == null) {
            this.teamSpawnLocations = new HashMap<>();
        }
        teamSpawnLocations.put(teamType, location);
    }

    public boolean removeTeamSpawnLocation(@NotNull BedWarsTeamType teamType) {
        return Optional.ofNullable(teamSpawnLocations)
                .map(teamSpawnLocations -> teamSpawnLocations.remove(teamType) != null)
                .orElse(false);
    }

    public Optional<BedWarsMapDirectedLocation> getTeamSpawnLocation(@NotNull BedWarsTeamType teamType) {
        return Optional.ofNullable(teamSpawnLocations)
                .flatMap(teamSpawnLocations -> Optional.ofNullable(teamSpawnLocations.get(teamType)));
    }

    public void addTeamBedLocation(@NotNull BedWarsTeamType teamType, @NotNull BedWarsMapLocation location) {
        if (teamBedLocations == null) {
            this.teamBedLocations = new HashMap<>();
        }
        teamBedLocations.put(teamType, location);
    }

    public boolean removeTeamBedLocation(@NotNull BedWarsTeamType teamType) {
        return Optional.ofNullable(teamBedLocations)
                .map(teamBedLocations -> teamBedLocations.remove(teamType) != null)
                .orElse(false);
    }

    public Optional<BedWarsMapLocation> getTeamBedLocation(@NotNull BedWarsTeamType teamType) {
        return Optional.ofNullable(teamBedLocations)
                .flatMap(teamBedLocations -> Optional.ofNullable(teamBedLocations.get(teamType)));
    }

    public int addShopLocation(@NotNull BedWarsMapDirectedLocation location) {
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

    public int addSpawnerLocation(@NotNull BedWarsResourceSpawnerType spawnerType, @NotNull BedWarsMapDirectedLocation location) {
        if (spawnerLocations == null) {
            this.spawnerLocations = new HashMap<>();
        }

        Map<Integer, BedWarsMapLocation> idLocations = spawnerLocations.computeIfAbsent(spawnerType, o -> new HashMap<>());
        System.out.println("size : " + idLocations.size());
        final int id = idLocations.size() + 1;
        System.out.println("id : " + id);
        idLocations.put(id, location);
        return id;
    }

    public boolean removeSpawnerLocation(@NotNull BedWarsResourceSpawnerType spawnerType, int id) {
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

            List<Integer> keysToUpdate = map.keySet().stream()
                    .filter(i -> i > id)
                    .toList();
            keysToUpdate.forEach(i -> map.put(i - 1, map.remove(i)));
            return true;
        }).orElse(false);
    }

    public void setRespawnLocation(@NotNull BedWarsMapDirectedLocation location) {
        this.respawnLocation = location;
    }

    public void setSpectatorLocation(@NotNull BedWarsMapDirectedLocation location) {
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
