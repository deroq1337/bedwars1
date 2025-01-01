package com.github.deroq1337.bedwars.data.game.spawners;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public enum BedWarsResourceSpawnerType {

    BRONZE("resource_bronze_name", Material.BRICK),
    IRON("resource_iron_name", Material.IRON_INGOT),
    GOLD("resource_gold_name", Material.GOLD_INGOT);

    private final @NotNull String name;
    private final @NotNull Material material;
}
