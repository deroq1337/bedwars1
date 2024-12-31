package com.github.deroq1337.bedwars.data.game.map;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BedWarsMapManager {

    @NotNull CompletableFuture<Boolean> saveMap(@NotNull BedWarsMap map);

    @NotNull CompletableFuture<Boolean> deleteMap(@NotNull String name);

    @NotNull CompletableFuture<Optional<BedWarsMap>> getMapByName(@NotNull String name);

    @NotNull CompletableFuture<List<BedWarsMap>> getRandomMaps(int count);

    @NotNull CompletableFuture<List<BedWarsMap>> getMaps();
}
