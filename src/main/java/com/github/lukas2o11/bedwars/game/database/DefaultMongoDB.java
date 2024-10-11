package com.github.lukas2o11.bedwars.game.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

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
    public <T> MongoCollection<T> getMongoCollection(final String name, final Class<T> entityClass) {
        return database.getCollection(name, entityClass);
    }

    private MongoClientSettings buildSettings() {
        final ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
                .automatic(true)
                .build());
        final CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        return MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
    }
}
