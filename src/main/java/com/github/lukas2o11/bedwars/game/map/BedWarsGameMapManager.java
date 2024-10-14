package com.github.lukas2o11.bedwars.game.map;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BedWarsGameMapManager<M extends BedWarsGameMap> {

    CompletableFuture<Boolean> createMap(final M map);

    CompletableFuture<Boolean> updateMap(final M map);

    CompletableFuture<Boolean> deleteMap(final String name);

    CompletableFuture<Optional<M>> getMapByName(final String name);

    CompletableFuture<Optional<M>> getRandomMap();

    CompletableFuture<List<M>> listMaps();
}
