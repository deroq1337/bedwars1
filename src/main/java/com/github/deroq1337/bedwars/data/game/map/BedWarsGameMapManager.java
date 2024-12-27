package com.github.deroq1337.bedwars.data.game.map;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BedWarsGameMapManager {

    @NotNull CompletableFuture<Boolean> createMap(@NotNull BedWarsGameMap map);

    @NotNull CompletableFuture<Boolean> updateMap(@NotNull BedWarsGameMap map);

    @NotNull CompletableFuture<Boolean> deleteMap(@NotNull String name);

    @NotNull CompletableFuture<Optional<BedWarsGameMap>> getMapByName(@NotNull String name);

    @NotNull CompletableFuture<List<BedWarsGameMap>> getRandomMaps(int count);

    @NotNull CompletableFuture<List<BedWarsGameMap>> getMaps();
}
