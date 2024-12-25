package com.github.deroq1337.bedwars.data.game.map;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BedWarsGameMapManager<M extends BedWarsGameMap> {

    @NotNull CompletableFuture<Boolean> createMap(@NotNull M map);

    @NotNull CompletableFuture<Boolean> updateMap(@NotNull M map);

    @NotNull CompletableFuture<Boolean> deleteMap(@NotNull String name);

    @NotNull CompletableFuture<Optional<M>> getMapByName(@NotNull String name);

    @NotNull CompletableFuture<List<M>> getRandomMaps(int count);

    @NotNull CompletableFuture<List<M>> getMaps();
}
