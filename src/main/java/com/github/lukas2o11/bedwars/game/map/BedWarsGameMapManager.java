package com.github.lukas2o11.bedwars.game.map;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BedWarsGameMapManager<M extends BedWarsGameMap> {

    CompletableFuture<Boolean> createMap(@NotNull M map);

    CompletableFuture<Boolean> updateMap(@NotNull M map);

    CompletableFuture<Boolean> deleteMap(@NotNull String name);

    CompletableFuture<Optional<M>> getMapByName(@NotNull String name);

    CompletableFuture<List<M>> getRandomMaps(int count);

    CompletableFuture<List<M>> getMaps();
}
