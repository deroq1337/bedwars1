package com.github.lukas2o11.bedwars.game.map;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BedWarsGameMapManager {

    CompletableFuture<Boolean> createMap(final BedWarsGameMap map);

    CompletableFuture<Boolean> updateMap(final BedWarsGameMap map);

    CompletableFuture<Boolean> deleteMap(final BedWarsGameMap map);

    CompletableFuture<Optional<BedWarsGameMap>> getMapByName(final String name);

    CompletableFuture<Optional<BedWarsGameMap>> getRandomMap();

    CompletableFuture<List<BedWarsGameMap>> listMaps();
}
