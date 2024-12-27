package com.github.deroq1337.bedwars.data.config;

import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;

public interface ConfigManager {

    @NotNull Configuration getConfig();

    void save();
}
