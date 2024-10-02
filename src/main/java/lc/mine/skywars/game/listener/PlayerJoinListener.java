package lc.mine.skywars.game.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.map.MapSpawn;

public class PlayerJoinListener implements Listener {

    private final ConfigManager manager;
    
    public PlayerJoinListener(ConfigManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (manager.map == null || manager.map.spawns() == null) {
            return;
        }
        final MapSpawn[] spawns = manager.map.spawns();

        for (final MapSpawn spawn : spawns) {
            if (spawn.playerUsingIt == null) {
                spawn.playerUsingIt = event.getPlayer().getUniqueId();
                teleport(event.getPlayer(), spawn, manager.map.world());
                return;
            }
        }
        event.getPlayer().kickPlayer(Messages.get("game-full"));
    }

    private void teleport(final Player player, final MapSpawn spawn, final World world) {
        final int x = spawn.x;
        final int y = spawn.y;
        final int z = spawn.z;

        player.teleport(new Location(world, x, y, z));
        
        world.getBlockAt(x, y-1, z).setType(Material.GLASS);
        world.getBlockAt(x, y+2, z).setType(Material.GLASS);
    
        world.getBlockAt(x-1, y, z).setType(Material.GLASS);
        world.getBlockAt(x+1, y, z).setType(Material.GLASS);

        world.getBlockAt(z-1, y, z).setType(Material.GLASS);
        world.getBlockAt(z+1, y, z).setType(Material.GLASS);
    }
}
