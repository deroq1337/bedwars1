package com.github.deroq1337.bedwars;

import com.github.deroq1337.bedwars.data.config.ConfigManager;
import com.github.deroq1337.bedwars.data.config.DefaultConfigManager;
import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.DefaultBedWarsGame;
import com.github.deroq1337.bedwars.data.database.DefaultMongoDB;
import com.github.deroq1337.bedwars.data.database.MongoDB;
import com.github.deroq1337.bedwars.data.language.DefaultLanguageManager;
import com.github.deroq1337.bedwars.data.language.LanguageManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class BedWars extends JavaPlugin {

    private MongoDB mongoDB;
    private ConfigManager mainConfigManager;
    private LanguageManager languageManager;
    private BedWarsGame game;

    @Override
    public void onEnable() {
        this.mongoDB = new DefaultMongoDB();
        mongoDB.connect();

        this.mainConfigManager = new DefaultConfigManager(new File("plugins/bedwars/configs/config.yml"));

        this.languageManager = new DefaultLanguageManager(new File("plugins/bedwars/locales"));
        languageManager.loadMessages().join();

        this.game = new DefaultBedWarsGame(this);
    }

    @Override
    public void onDisable() {
        languageManager.clearMessages();
        mongoDB.disconnect();
    }
}
