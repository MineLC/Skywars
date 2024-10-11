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
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.top.TopManager;
import lc.mine.skywars.map.MapSpawn;

public class PlayerLeaveGameListener implements Listener {

    private final JavaPlugin plugin;
    private final GameManager gameManager;
    private final TopManager topManager;

    public PlayerLeaveGameListener(GameManager gameManager, TopManager topManager, JavaPlugin plugin) {
        this.gameManager = gameManager;
        this.plugin = plugin;
        this.topManager = topManager;
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
        final User victimData = SkywarsDatabase.getDatabase().getCached(event.getVictim().getUniqueId());
        victimData.deaths++;
        topManager.calculateDeaths(victimData, event.getVictim().getName());

        if (event.getKiller() != null) {
            final User killerData = SkywarsDatabase.getDatabase().getCached(event.getKiller().getUniqueId());
            killerData.kills++;
            topManager.calculateKills(killerData, event.getKiller().getName());
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        SkywarsDatabase.getDatabase().save(event.getPlayer());
        gameManager.quit(event.getPlayer());
    }
}