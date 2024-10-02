package lc.mine.skywars.map;

import java.util.UUID;

public class MapSpawn {
    
    public final int x, y, z;
    public UUID playerUsingIt;

    MapSpawn(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
