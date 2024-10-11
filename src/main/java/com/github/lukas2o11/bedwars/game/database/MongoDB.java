package com.github.lukas2o11.bedwars.game.database;

import com.mongodb.client.MongoCollection;

public interface MongoDB {

    void connect();

    void disconnect();

    <T> MongoCollection<T> getMongoCollection(final String name, final Class<T> entityClass);
}
