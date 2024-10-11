package lc.mine.skywars.map;

import org.bukkit.World;

public record SkywarsMap(
    MapSpawn[] spawns,
    String displayName,
    World world
) {
    public void resetSpawns() {
        for (final MapSpawn spawn : spawns) {
            spawn.playerUsingIt = null;
        }
    }
}