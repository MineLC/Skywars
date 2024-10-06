package lc.mine.skywars.game.cage;

import org.bukkit.Material;
import org.bukkit.World;

public class GameCage {
    
    public static void build(final World world, final int x, final int y, final int z, final Material material) {
        world.getBlockAt(x, y-1, z).setType(material);
        world.getBlockAt(x, y+2, z).setType(material);
    
        world.getBlockAt(x-1, y, z).setType(material);
        world.getBlockAt(x+1, y, z).setType(material);

        world.getBlockAt(x, y, z-1).setType(material);
        world.getBlockAt(x, y, z+1).setType(material);
    }

    public static void delete(final World world, final int x, final int y, final int z) {
        build(world, x, y, z, Material.AIR);
    }
}
