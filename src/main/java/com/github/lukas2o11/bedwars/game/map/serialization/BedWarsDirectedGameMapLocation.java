package com.github.lukas2o11.bedwars.game.map.serialization;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BedWarsDirectedGameMapLocation extends BedWarsGameMapLocation {

    private final float yaw;
    private final float pitch;

    public BedWarsDirectedGameMapLocation(final Location location) {
        super(location);
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(getWorld()), getX(), getY(), getZ(), yaw, pitch);
    }
}
