package lc.mine.skywars.game.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.combatlog.events.PlayerDeathCombatLog;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.map.MapSpawn;

public class PlayerLeaveGameListener implements Listener {

    private final JavaPlugin plugin;
    private final GameManager gameManager;

    public PlayerLeaveGameListener(GameManager gameManager, JavaPlugin plugin) {
        this.gameManager = gameManager;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(final EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        final SkywarsGame game = gameManager.getGame(player);
        if (game == null) {
            return;
        }
        player.setGameMode(GameMode.SPECTATOR);
        Messages.sendNoGet(game.getPlayers(), Messages.get("death"));
        final MapSpawn spawn = game.getMap().spawns()[0];

        gameManager.tryFindWinner(game);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.teleport(new Location(game.getMap().world(), spawn.x, spawn.y, spawn.z)), 2);
    }

    @EventHandler
    public void onPlayerCombatLog(final PlayerDeathCombatLog event) {
        SkywarsDatabase.getDatabase().getCached(event.getVictim().getUniqueId()).deaths++;
        if (event.getKiller() != null) {
            SkywarsDatabase.getDatabase().getCached(event.getKiller().getUniqueId()).kills++;
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        SkywarsDatabase.getDatabase().save(event.getPlayer());
        gameManager.quit(event.getPlayer());
    }
}