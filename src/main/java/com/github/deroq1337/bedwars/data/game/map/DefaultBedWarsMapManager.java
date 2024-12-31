package com.github.deroq1337.bedwars.data.game.map;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DefaultBedWarsMapManager implements BedWarsMapManager {

    private final @NotNull File mapsDirectory;

    public DefaultBedWarsMapManager(@NotNull BedWarsGame game) {
        this.mapsDirectory = new File("plugins/bedwars/maps/");
        mapsDirectory.mkdirs();
    }

    @Override
    public @NotNull CompletableFuture<Boolean> saveMap(@NotNull BedWarsMap map) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                map.save();
                return true;
            } catch (InvalidConfigurationException e) {
                System.err.println("Could not save map: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteMap(@NotNull String name) {
        return CompletableFuture.supplyAsync(() -> {
            return new BedWarsMap(name).delete();
        });
    }

    @Override
    public @NotNull CompletableFuture<Optional<BedWarsMap>> getMapByName(@NotNull String name) {
        return CompletableFuture.supplyAsync(() -> {
            BedWarsMap map = new BedWarsMap(name);
            if (!map.exists()) {
                return Optional.empty();
            }

            try {
                map.load();
                return Optional.of(map);
            } catch (InvalidConfigurationException e) {
                System.err.println("Could not load map: " + e.getMessage());
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<List<BedWarsMap>> getRandomMaps(int count) {
        return CompletableFuture.supplyAsync(() -> {
            return Optional.ofNullable(mapsDirectory.listFiles()).map(files -> {
                List<File> fileList = new ArrayList<>(Arrays.stream(files).toList());
                Collections.shuffle(fileList);

                return Arrays.stream(files)
                        .limit(Math.min(count, files.length))
                        .map(file -> new BedWarsMap(file, true))
                        .toList();
            }).orElse(Collections.emptyList());
        });
    }

    @Override
    public @NotNull CompletableFuture<List<BedWarsMap>> getMaps() {
        return CompletableFuture.supplyAsync(() -> {
            return Optional.ofNullable(mapsDirectory.listFiles()).map(files -> {
                return Arrays.stream(files)
                        .map(file -> new BedWarsMap(file, true))
                        .toList();
            }).orElse(Collections.emptyList());
        });
    }
}