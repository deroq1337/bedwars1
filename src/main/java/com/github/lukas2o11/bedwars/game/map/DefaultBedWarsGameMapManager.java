package com.github.lukas2o11.bedwars.game.map;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DefaultBedWarsGameMapManager implements BedWarsGameMapManager {

    private static final String MAP_COLLECTION_NAME = "maps";

    private final MongoCollection<BedWarsGameMap> mapCollection;

    public DefaultBedWarsGameMapManager(BedWarsGame game) {
        this.mapCollection = game.getBedWars().getMongoDB().getMongoCollection(MAP_COLLECTION_NAME, BedWarsGameMap.class);
    }

    @Override
    public CompletableFuture<Boolean> createMap(final BedWarsGameMap map) {
        return CompletableFuture.supplyAsync(() -> {
            final InsertOneResult result = mapCollection.insertOne(map);
            return result.wasAcknowledged();
        });
    }

    @Override
    public CompletableFuture<Boolean> updateMap(final BedWarsGameMap map) {
        return CompletableFuture.supplyAsync(() -> {
            final Bson filter = Filters.eq("_id", map.getId());
            final UpdateResult result = mapCollection.replaceOne(filter, map);
            return result.getModifiedCount() != 0;
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteMap(final String name) {
        return CompletableFuture.supplyAsync(() -> {
            final Bson filter = buildNameFilter(name);
            final DeleteResult result = mapCollection.deleteMany(filter);
            return result.getDeletedCount() != 0;
        });
    }

    @Override
    public CompletableFuture<Optional<BedWarsGameMap>> getMapByName(final String name) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(mapCollection.find(buildNameFilter(name)).first()));
    }

    @Override
    public CompletableFuture<Optional<BedWarsGameMap>> getRandomMap() {
        return CompletableFuture.supplyAsync(() -> {
            final List<Bson> aggregates = Collections.singletonList(Aggregates.sample(1));
            return Optional.ofNullable(mapCollection.aggregate(aggregates).first());
        });
    }

    @Override
    public CompletableFuture<List<BedWarsGameMap>> listMaps() {
        return CompletableFuture.supplyAsync(() -> Collections.unmodifiableList(mapCollection.find().into(new ArrayList<>())));
    }

    private Bson buildNameFilter(final String name) {
        final String regex = "^" + name + "$";
        return Filters.regex("name", regex, "i");
    }
}