package com.github.lukas2o11.bedwars.game.map.serialization;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BedWarsGameMapLocation {

    private String world;
    private final double x;
    private final double y;
    private final double z;

    public BedWarsGameMapLocation(final Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }
}
