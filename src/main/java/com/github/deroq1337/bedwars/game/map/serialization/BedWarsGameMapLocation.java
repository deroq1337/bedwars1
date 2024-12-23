package com.github.deroq1337.bedwars.game.map.serialization;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BedWarsGameMapLocation {

    private @NotNull final String world;
    private final double x;
    private final double y;
    private final double z;

    public BedWarsGameMapLocation(@NotNull Location location) {
        this.world = Optional.ofNullable(location.getWorld())
                .map(WorldInfo::getName)
                .orElseThrow(() -> new IllegalStateException("BedWarsGameMapLocation: world of bukkit location is null"));
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public @NotNull Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }
}
