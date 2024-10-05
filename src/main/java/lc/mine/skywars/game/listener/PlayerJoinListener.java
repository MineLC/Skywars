package lc.mine.skywars.game.listener;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.ichocomilk.lightsidebar.nms.v1_8R3.Sidebar1_8R3;
import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.game.GameState;
import lc.mine.skywars.map.GameMap;
import lc.mine.skywars.map.MapSpawn;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class PlayerJoinListener implements Listener {

    private final ConfigManager manager;
    
    public PlayerJoinListener(ConfigManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final GameMap map = manager.getMap();
        if (map == null || map.spawns() == null || map.spawns().length == 0) {
            return;
        }

        if (GameState.currentState != GameState.PREGAME) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            final MapSpawn spawn = map.spawns()[0];
            event.getPlayer().teleport(new Location(map.world(), spawn.x, spawn.y, spawn.z));
            return;
        }
        final MapSpawn[] spawns = map.spawns();
        MapSpawn spawnAvailable = null;
        for (final MapSpawn spawn : spawns) {
            if (spawn.playerUsingIt == null) {
                spawnAvailable = spawn;
                break;
            }
        }
        if (spawnAvailable == null) {
            event.getPlayer().kickPlayer(Messages.get("game-full"));
        }

        final MapSpawn immutableSpawn = spawnAvailable;
        manager.getDatabase().load(event.getPlayer(), () -> {
            immutableSpawn.playerUsingIt = event.getPlayer().getUniqueId();
            teleport(event.getPlayer(), immutableSpawn, map.world());
            sendPreGameSidebar();
            addPregameItems(event.getPlayer().getInventory());
            Messages.send(event.getPlayer(), "join");
        });
    }

    private void teleport(final Player player, final MapSpawn spawn, final World world) {
        final int x = spawn.x;
        final int y = spawn.y;
        final int z = spawn.z;

        player.teleport(new Location(world, x+0.5, y, z+0.5));
        
        world.getBlockAt(x, y-1, z).setType(Material.GLASS);
        world.getBlockAt(x, y+2, z).setType(Material.GLASS);
    
        world.getBlockAt(x-1, y, z).setType(Material.GLASS);
        world.getBlockAt(x+1, y, z).setType(Material.GLASS);

        world.getBlockAt(x, y, z-1).setType(Material.GLASS);
        world.getBlockAt(x, y, z+1).setType(Material.GLASS);
    }

    private void sendPreGameSidebar() {
        final GameMap map = manager.getMap();
        final Sidebar1_8R3 sidebar = new Sidebar1_8R3();
        final List<EntityPlayer> players = MinecraftServer.getServer().getPlayerList().players;
        final int amountPlayers = players.size();
        sidebar.setTitle("§6§lSkywars");

        for (final EntityPlayer player : players) {
            final Player bukkitPlayer = player.getBukkitEntity();
            sidebar.setLines(sidebar.createLines(new String[]{
                "",
                "§fJugadores: §a" + amountPlayers + '/' + map.spawns().length,
                "",
                "§fMapa: " + map.displayName() ,
                "",
                "§bplay.mine.lc"
            }));
            sidebar.sendLines(bukkitPlayer);
            sidebar.sendTitle(bukkitPlayer);
        }
    }

    private void addPregameItems(final PlayerInventory inventory) {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Tipos de cofres");
        item.setItemMeta(meta);

        inventory.setItem(1, item);

        item = new ItemStack(Material.BOW);
        meta = item.getItemMeta();
        meta.setDisplayName("§eKits");
        item.setItemMeta(meta);

        inventory.setItem(0, item);
    }
}