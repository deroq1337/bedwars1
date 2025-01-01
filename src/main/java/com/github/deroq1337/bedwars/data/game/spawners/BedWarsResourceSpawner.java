package com.github.deroq1337.bedwars.data.game.spawners;

import com.github.deroq1337.bedwars.data.game.BedWarsGame;
import com.github.deroq1337.bedwars.data.game.spawners.models.ResourceSpawnerSettings;
import com.github.deroq1337.bedwars.data.game.item.ItemBuilders;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
public class BedWarsResourceSpawner {

    private final @NotNull BedWarsGame game;
    private final @NotNull BedWarsResourceSpawnerType spawnerType;
    private final @NotNull ResourceSpawnerSettings settings;
    private final @NotNull Location location;
    private final @NotNull ItemStack item;

    private Optional<BukkitTask> task = Optional.empty();

    public BedWarsResourceSpawner(@NotNull BedWarsGame game, @NotNull BedWarsResourceSpawnerType spawnerType, @NotNull Location location) {
        this.game = game;
        this.spawnerType = spawnerType;
        this.settings = Optional.ofNullable(game.getResourceSpawnerConfig().getSettings().get(spawnerType))
                .orElseThrow(() -> new NoSuchElementException("No setting for spawner '" + spawnerType + "' found"));
        this.location = location;
        this.item = ItemBuilders.normal(spawnerType.getMaterial())
                .title(spawnerType.getName())
                .build();
    }

    public void start() {
        this.task = Optional.of(new BukkitRunnable() {
            @Override
            public void run() {
                Optional.ofNullable(location.getWorld()).ifPresent(world -> dropItem(world));
            }
        }.runTaskTimer(game.getBedWars(), settings.getPeriod(), settings.getPeriod()));
    }

    public void stop() {
        task.ifPresent(BukkitTask::cancel);
        this.task = Optional.empty();
    }

    private void dropItem(@NotNull World world) {
        world.dropItemNaturally(location, item).setUnlimitedLifetime(true);
    }
}
