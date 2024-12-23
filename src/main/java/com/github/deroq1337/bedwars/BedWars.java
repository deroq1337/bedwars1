package com.github.deroq1337.bedwars;

import com.github.deroq1337.bedwars.game.DefaultBedWarsGame;
import com.github.deroq1337.bedwars.game.database.DefaultMongoDB;
import com.github.deroq1337.bedwars.game.database.MongoDB;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

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
        mongoDB.disconnect();
    }
}
