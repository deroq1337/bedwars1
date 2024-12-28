package com.github.deroq1337.bedwars.data.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public abstract class Config {

    @ConfigIgnore
    private final @NotNull File file;

    @ConfigIgnore
    private final @NotNull YamlConfiguration configuration;

    public Config(@NotNull File file) {
        this.file = file;
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(ConfigIgnore.class))
                .forEach(this::writeFieldToConfig);

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            configuration.save(file);
        } catch (IOException e) {
            System.err.println("Could not save config: " + e.getMessage());
        }
    }

    public void load() {
        if (!file.exists()) {
            save();
        }

        Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(ConfigIgnore.class))
                .forEach(this::readFieldFromConfig);
    }

    private void writeFieldToConfig(@NotNull Field field) {
        try {
            configuration.set(getConfigPath(field), readField(field).orElse(null));
        } catch (Exception e) {
            System.err.println("Error getting value of field: " + e.getMessage());
        }
    }

    private void readFieldFromConfig(@NotNull Field field) {
        String configPath = getConfigPath(field);
        if (!configuration.contains(configPath)) {
            return;
        }

        try {
            field.setAccessible(true);
            field.set(this, configuration.get(configPath));
            field.setAccessible(false);
        } catch (Exception e) {
            System.err.println("Error setting value of field: " + e.getMessage());
        }
    }

    private Optional<Object> readField(@NotNull Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(this);
            field.setAccessible(false);

            return Optional.ofNullable(value);
        } catch (Exception e) {
            System.err.println("Error getting value of field: " + e.getMessage());
            return Optional.empty();
        }
    }

    private @NotNull String getConfigPath(@NotNull Field field) {
        return field.isAnnotationPresent(ConfigPath.class)
                ? field.getAnnotation(ConfigPath.class).value()
                : field.getName();
    }
}
