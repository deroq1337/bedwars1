package com.github.deroq1337.bedwars.data.game.map;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DefaultBedWarsGameMapManager implements BedWarsGameMapManager {

    private final @NotNull File mapsDirectory;

    public DefaultBedWarsGameMapManager(@NotNull BedWarsGame game) {
        this.mapsDirectory = new File("plugins/bedwars/maps/");
        mapsDirectory.mkdirs();
    }

    @Override
    public @NotNull CompletableFuture<Boolean> saveMap(@NotNull BedWarsGameMap map) {
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
            return new BedWarsGameMap(name).delete();
        });
    }

    @Override
    public @NotNull CompletableFuture<Optional<BedWarsGameMap>> getMapByName(@NotNull String name) {
        return CompletableFuture.supplyAsync(() -> {
            BedWarsGameMap gameMap = new BedWarsGameMap(name);
            if (!gameMap.exists()) {
                return Optional.empty();
            }

            try {
                gameMap.load();
                return Optional.of(gameMap);
            } catch (InvalidConfigurationException e) {
                System.err.println("Could not load map: " + e.getMessage());
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<List<BedWarsGameMap>> getRandomMaps(int count) {
        return CompletableFuture.supplyAsync(() -> {
            return Optional.ofNullable(mapsDirectory.listFiles()).map(files -> {
                List<File> fileList = new ArrayList<>(Arrays.stream(files).toList());
                Collections.shuffle(fileList);

                return Arrays.stream(files)
                        .limit(Math.min(count, files.length))
                        .map(file -> new BedWarsGameMap(file, true))
                        .toList();
            }).orElse(Collections.emptyList());
        });
    }

    @Override
    public @NotNull CompletableFuture<List<BedWarsGameMap>> getMaps() {
        return CompletableFuture.supplyAsync(() -> {
            return Optional.ofNullable(mapsDirectory.listFiles()).map(files -> {
                return Arrays.stream(files)
                        .map(file -> new BedWarsGameMap(file, true))
                        .toList();
            }).orElse(Collections.emptyList());
        });
    }
}