package com.github.deroq1337.bedwars.game.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jetbrains.annotations.NotNull;

public class DefaultMongoDB implements MongoDB {

    private MongoClient client;
    private MongoDatabase database;

    @Override
    public void connect() {
        final MongoClientSettings clientSettings = buildSettings();
        this.client = MongoClients.create(clientSettings);
        this.database = client.getDatabase("bedwars");
    }

    @Override
    public void disconnect() {
        if (client != null) {
            client.close();
        }
    }

    @Override
    public @NotNull <T> MongoCollection<T> getMongoCollection(@NotNull String name, @NotNull Class<T> entityClass) {
        return database.getCollection(name, entityClass);
    }

    private @NotNull MongoClientSettings buildSettings() {
        final ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
                .automatic(true)
                .build());
        final CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry
              //  CodecRegistries.fromProviders(new OptionalPropertyCodecProvider())
        );
        return MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
    }
}
