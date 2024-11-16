package com.github.lukas2o11.bedwars;

import com.github.lukas2o11.bedwars.game.DefaultBedWarsGame;
import com.github.lukas2o11.bedwars.game.database.DefaultMongoDB;
import com.github.lukas2o11.bedwars.game.database.MongoDB;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class BedWars extends JavaPlugin {

    @Getter
    private MongoDB mongoDB;

    @Override
    public void onEnable() {
        this.mongoDB = new DefaultMongoDB();
        mongoDB.connect();

        new DefaultBedWarsGame(this);
    }

    @Override
    public void onDisable() {
        Optional.of(mongoDB).ifPresent(MongoDB::disconnect);
    }
}
