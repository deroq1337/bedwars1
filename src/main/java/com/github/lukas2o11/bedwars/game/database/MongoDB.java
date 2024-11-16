package com.github.lukas2o11.bedwars.game.database;

import com.mongodb.client.MongoCollection;
import org.jetbrains.annotations.NotNull;

public interface MongoDB {

    void connect();

    void disconnect();

    <T> MongoCollection<T> getMongoCollection(@NotNull String name, @NotNull Class<T> entityClass);
}
