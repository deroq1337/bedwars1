package com.github.deroq1337.bedwars.data.database;

import com.mongodb.client.MongoCollection;
import org.jetbrains.annotations.NotNull;

public interface MongoDB {

    void connect();

    void disconnect();

    @NotNull <T> MongoCollection<T> getMongoCollection(@NotNull String name, @NotNull Class<T> entityClass);
}
