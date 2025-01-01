package com.github.deroq1337.bedwars.data.game.spawners.config;

import com.github.deroq1337.bedwars.data.game.spawners.models.ResourceSpawnerSettings;
import com.github.deroq1337.bedwars.data.game.map.converters.EnumConverter;
import com.github.deroq1337.bedwars.data.game.spawners.BedWarsResourceSpawnerType;
import com.github.deroq1337.bedwars.data.game.utils.EnumMapFix;
import lombok.Getter;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.cubespace.Yamler.Config.InvalidConverterException;
import net.cubespace.Yamler.Config.YamlConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ResourceSpawnerConfig extends YamlConfig {

    private @NotNull Map<BedWarsResourceSpawnerType, ResourceSpawnerSettings> settings = new HashMap<>(){{
        put(BedWarsResourceSpawnerType.BRONZE, new ResourceSpawnerSettings(20L));
        put(BedWarsResourceSpawnerType.IRON, new ResourceSpawnerSettings(10 * 20L));
        put(BedWarsResourceSpawnerType.GOLD, new ResourceSpawnerSettings(30 * 20L));
    }};

    public ResourceSpawnerConfig() {
        this.CONFIG_FILE = new File("plugins/bedwars/configs/spawners.yml");
        try {
            addConverter(EnumConverter.class);
            init();
        } catch (InvalidConfigurationException | InvalidConverterException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() throws InvalidConfigurationException {
        super.init();
        this.settings = EnumMapFix.fixMapKeys(settings, BedWarsResourceSpawnerType.class);
    }
}
