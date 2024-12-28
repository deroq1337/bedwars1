package com.github.deroq1337.bedwars.data.game.map.serialization;

import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BedWarsDirectedGameMapLocation extends BedWarsGameMapLocation {

    private float yaw;
    private float pitch;

    public BedWarsDirectedGameMapLocation(@NotNull Location location) {
        super(location);
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public @NotNull Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(getWorld()), getX(), getY(), getZ(), yaw, pitch);
    }
}
