package lc.mine.skywars.map;

import org.bukkit.World;

public interface MapLoadSupply {
    void onLoad(final World world, final SkywarsMap map);
    void onMapLoadError();
}
