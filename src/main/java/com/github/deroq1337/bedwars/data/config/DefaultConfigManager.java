package com.github.deroq1337.bedwars.data.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
public class DefaultConfigManager implements ConfigManager {

    private final @NotNull File file;
    private final @NotNull FileConfiguration config;

    public DefaultConfigManager(@NotNull File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);

        if (createFiles()) {
            saveDefaultConfig();
        }
    }

    @Override
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            System.err.println("Could not save config: " + e.getMessage());
        }
    }

    private boolean createFiles() {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                return file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Could not create files: " + e.getMessage());
        }
        return false;
    }

    private void saveDefaultConfig() {
        config.set("min_players", 1);
        config.set("map_collection_name", "maps");
        config.set("voting.map.slot", 4);
        config.set("voting.map.inventory.size", 9);
        config.set("voting.map.inventory.slots", List.of(3, 5));

        save();
    }
}
