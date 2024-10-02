package lc.mine.skywars.map;

import org.bukkit.World;

public record GameMap(
    MapSpawn[] spawns,
    String displayName,
    int worldborder,
    World world
) {}