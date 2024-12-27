package com.github.deroq1337.bedwars.data.game.map;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DefaultBedWarsGameMapManager implements BedWarsGameMapManager {

    private final @NotNull MongoCollection<BedWarsGameMap> mapCollection;

    public DefaultBedWarsGameMapManager(@NotNull BedWarsGame game) {
        String collectionName = Optional.ofNullable(game.getBedWars().getMainConfigManager().getConfig().getString("map_collection_name"))
                .orElse("maps");
        this.mapCollection = game.getBedWars().getMongoDB().getMongoCollection(collectionName, BedWarsGameMap.class);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> createMap(@NotNull BedWarsGameMap map) {
        return CompletableFuture.supplyAsync(() -> {
            final InsertOneResult result = mapCollection.insertOne(map);
            return result.wasAcknowledged();
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> updateMap(@NotNull BedWarsGameMap map) {
        return CompletableFuture.supplyAsync(() -> {
            final Bson filter = Filters.eq("_id", map.getId());
            final UpdateResult result = mapCollection.replaceOne(filter, map);
            return result.getModifiedCount() != 0;
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteMap(@NotNull String name) {
        return CompletableFuture.supplyAsync(() -> {
            final Bson filter = buildNameFilter(name);
            final DeleteResult result = mapCollection.deleteMany(filter);
            return result.getDeletedCount() != 0;
        });
    }


    @Override
    public @NotNull CompletableFuture<Optional<BedWarsGameMap>> getMapByName(@NotNull String name) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(mapCollection.find(buildNameFilter(name)).first()));
    }

    @Override
    public @NotNull CompletableFuture<List<BedWarsGameMap>> getRandomMaps(int count) {
        return CompletableFuture.supplyAsync(() -> {
            final List<Bson> aggregates = Collections.singletonList(Aggregates.sample(count));
            return mapCollection.aggregate(aggregates).into(new ArrayList<>());
        });
    }

    @Override
    public @NotNull CompletableFuture<List<BedWarsGameMap>> getMaps() {
        return CompletableFuture.supplyAsync(() -> Collections.unmodifiableList(mapCollection.find().into(new ArrayList<>())));
    }

    private @NotNull Bson buildNameFilter(@NotNull String name) {
        final String regex = "^" + name + "$";
        return Filters.regex("name", regex, "i");
    }
}