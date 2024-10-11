package lc.mine.skywars.game.listener;

import java.util.Set;

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
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.PlayerInGame;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.cage.GameCage;
import lc.mine.skywars.map.SkywarsMap;
import lc.mine.skywars.map.MapSpawn;

public class PlayerJoinListener implements Listener {

    private final GameManager gameManager;
    
    public PlayerJoinListener(final GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (!gameManager.haveMaps()) {
            return;
        }   
        final SkywarsGame game = gameManager.getGames()[0];   
        if (game.hasStarted()) {
            gameManager.join(event.getPlayer(), game);
            return;
        }

        final MapSpawn[] spawns = game.getMap().spawns();
        MapSpawn spawnAvailable = null;
        for (final MapSpawn spawn : spawns) {
            if (spawn.playerUsingIt == null) {
                spawnAvailable = spawn;
                break;
            }
        }
        if (spawnAvailable == null) {
            Messages.send(event.getPlayer(), "game-full");
            return;
        }
   
        final MapSpawn immutableSpawn = spawnAvailable;
        spawnAvailable.playerUsingIt = event.getPlayer().getUniqueId();

        SkywarsDatabase.getDatabase().load(event.getPlayer(), (user) -> {
            gameManager.join(event.getPlayer(), game);

            teleport(event.getPlayer(), immutableSpawn, game.getMap().world(), user.cageMaterial);

            sendPreGameSidebar(game);
            addPregameItems(event.getPlayer().getInventory());

            Messages.send(event.getPlayer(), "join");
        });
    }

    private void teleport(final Player player, final MapSpawn spawn, final World world, final Material cageMaterial) {
        final int x = spawn.x;
        final int y = spawn.y;
        final int z = spawn.z;

        player.teleport(new Location(world, x+0.5, y, z+0.5));
        GameCage.build(world, x, y, z, cageMaterial);
    }

    private void sendPreGameSidebar(final SkywarsGame game) {
        final SkywarsMap map = game.getMap();
        final Sidebar1_8R3 sidebar = new Sidebar1_8R3();
        final Set<PlayerInGame> players = game.getPlayers();
        final int amountPlayers = players.size();

        sidebar.setTitle("§6§lSkywars");
        sidebar.setLines(sidebar.createLines(new String[]{
            "",
            "§fJugadores: §a" + amountPlayers + '/' + map.spawns().length,
            "",
            "§fMapa: " + map.displayName() ,
            "",
            "§bplay.mine.lc"
        }));

        for (final PlayerInGame player : players) {
            final Player bukkitPlayer = player.getPlayer();
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
        
        item = new ItemStack(Material.BEACON);
        meta = item.getItemMeta();
        meta.setDisplayName("§bCajas");
        item.setItemMeta(meta);

        inventory.setItem(8, item);
    }
}